//
// Created by Malik Hiraev on 03.11.2019.
//

#include "chatty.h"

static unsigned messages_num = 0;

static Message *first_message = NULL;

static remove_message_if_needed() {
    if (messages_num >= DEFAULT_CHAT_HEIGHT) {
        first_message = first_message->next;
        free(first_message->text);
        free(first_message);
    }
};

void init() {
    printf("*****Chatty*****\n");
    for (int i = 0; i < DEFAULT_CHAT_HEIGHT; ++i) {
        printf("\n");
    }
    printf("****************\n");
}

void add_message(char *text) {
    Message *new_message = (Message *) malloc(sizeof(Message));
    char *message_text = malloc(sizeof(char) * DEFAULT_BUFFER_SIZE);
    strcpy(message_text, text);
    new_message = (Message) {message_text, NULL};
    messages_num++;
    remove_message_if_needed();
}

void print() {

}
