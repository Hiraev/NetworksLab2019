//
// Created by Malik Hiraev on 07.10.2019.
//

#include <stdio.h>
#include <stdlib.h>
#include <netdb.h>
#include <unistd.h>
#include <string.h>
#include <pthread.h>
#include <ncurses.h>

#include "../common/common.h"

#define MSG_BUFF_SIZE 512
#define DEL_CODE 127

void *terminal_reader(void *args);

void read_message(int fd);

char terminal_buff[MSG_BUFF_SIZE];

void print_user_input_if_needed();

pthread_mutex_t lock;

int main(int argc, char *argv[]) {
    int sockfd, n;
    uint16_t portno;
    struct sockaddr_in serv_addr;
    struct hostent *server;

    if (argc < 3) {
        fprintf(stderr, "usage %s hostname port\n", argv[0]);
        exit(0);
    }

    portno = (uint16_t) atoi(argv[2]);

    /* Create a socket point */
    sockfd = socket(AF_INET, SOCK_STREAM, 0);

    if (sockfd < 0) {
        perror("ERROR opening socket");
        exit(1);
    }

    server = gethostbyname(argv[1]);

    if (server == NULL) {
        fprintf(stderr, "ERROR, no such host\n");
        exit(0);
    }

    bzero((char *) &serv_addr, sizeof(serv_addr));
    serv_addr.sin_family = AF_INET;
    bcopy(server->h_addr, (char *) &serv_addr.sin_addr.s_addr, (size_t) server->h_length);
    serv_addr.sin_port = htons(portno);

    /* Now connect to the server */
    if (connect(sockfd, (struct sockaddr *) &serv_addr, sizeof(serv_addr)) < 0) {
        perror("ERROR connecting");
        exit(1);
    }

    // Читаем имя
    printf("Введите свое имя\n");
    char user_name[USER_NAME_SIZE];
    bzero(user_name, USER_NAME_SIZE);
    fgets(user_name, USER_NAME_SIZE, stdin);
    replace(user_name, USER_NAME_SIZE, '\n', '\0');
    /* Send message to the server */
    n = write(sockfd, user_name, USER_NAME_SIZE);
    if (n <= 0) {
        perror("ERROR writing to socket");
        exit(1);
    }

    //Init mutex
    if (pthread_mutex_init(&lock, NULL) != 0) {
        perror("ERROR creating mutex for clients list");
        exit(1);
    }

    pthread_t thread_id;
    pthread_create(&thread_id, NULL, terminal_reader, &sockfd);

    while (1) {
        /* Now read server response */
        read_message(sockfd);
    }
    endwin();
    return 0;
}

void *terminal_reader(void *args) {
    ssize_t n;
    size_t len = 0;
    initscr();
    int sockfd = *(int *) args;
    while (1) {
        noecho();
        bzero(terminal_buff, MSG_BUFF_SIZE);
        char sym = ' ';
        while (sym != '\n' && len != MSG_BUFF_SIZE) {
            int key_code = getch();
            sym = (char) key_code;
            if (key_code == DEL_CODE) {
                if (len > 0) {
                    move(0, --len);
                    terminal_buff[len] = '\0';
                }
            } else if (key_code > 31) {
                terminal_buff[len] = sym;
                len++;
            }
            if (key_code > 31) {
                pthread_mutex_lock(&lock);
                clrtoeol();
                refresh();
                printw("\r%s", terminal_buff);
                pthread_mutex_unlock(&lock);
            }
        }
        if (len == 0) continue;
        pthread_mutex_lock(&lock);
        echo();
        clrtoeol();
        refresh();
        attron(A_BOLD);
        printw("\rYou : %s", terminal_buff);
        attroff(A_BOLD);
        insertln();
        move(0, 0);
        /* Send message to the server */
        n = write(sockfd, &len, MSG_SIZE_VAL); // Передаем размер сообщения
        if (n <= 0) {
            perror("ERROR writing to socket");
            endwin();
            exit(1);
        }
        n = write(sockfd, &terminal_buff, len); // Передаем само сообщение
        if (n <= 0) {
            perror("ERROR writing to socket");
            endwin();
            exit(1);
        }
        pthread_mutex_unlock(&lock);
        len = 0;
    }
}

void read_message(int fd) {
    ssize_t n;
    size_t msg_size = 0;
    // Читаем размер сообщения
    n = readn(fd, (char *) &msg_size, MSG_SIZE_VAL);
    if (n <= 0) { // Клиент умер/отключился
        perror("ERROR reading from socket");
        endwin();
        exit(1);
    } else {
        // Читаем само сообщение
        char *msg = calloc((msg_size + 1), sizeof(char));
        bzero(msg, msg_size);
        //Читаем то кол-во сиволов, которое указано в заголовке сообщения
        readn(fd, msg, msg_size);
        pthread_mutex_lock(&lock);
        printw("\r%s", msg);
        move(0, 0);
        insertln();
        refresh();
        print_user_input_if_needed();
        pthread_mutex_unlock(&lock);
        free(msg);
    }
}

void print_user_input_if_needed() {
    if (strlen(terminal_buff) > 0) {
        printw("%s", terminal_buff);
        refresh();
    }
}
