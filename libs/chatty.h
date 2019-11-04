//
// Created by Malik Hiraev on 03.11.2019.
//

#ifndef NETWORKSLAB2019_CHATTY_H
#define NETWORKSLAB2019_CHATTY_H

#define DEFAULT_BUFFER_SIZE 512
#define DEFAULT_CHAT_HEIGHT 10

typedef struct Message {
    char *text;
    Message *next;
} Message;

void init();

void add_message(char *text);

void print();

#endif //NETWORKSLAB2019_CHATTY_H
