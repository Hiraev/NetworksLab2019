//
// Created by Malik Hiraev on 10/11/2019.
//

#ifndef NETWORKSLAB2019_FDS_WORKER_H
#define NETWORKSLAB2019_FDS_WORKER_H

#include "server.h"

#define POLL_TIMEOUT (-1) //poll blocks indefinitely

void init_fds_worker(int socket_fd);

unsigned get_fds_size();

void close_all_fds();

int poll_inner();

int add_fd(int fd);

void remove_and_close_fd(int index);

struct pollfd *get_pollfd(int index);

int get_acceptor_revents();

#endif //NETWORKSLAB2019_FDS_WORKER_H