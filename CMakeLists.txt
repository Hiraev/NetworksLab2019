cmake_minimum_required(VERSION 3.9)
project(untitled1)

set(CMAKE_CXX_STANDARD 11)

add_executable(NetworksLab2019 task1/client/client.c)
target_link_libraries(NetworksLab2019 readline)