cmake_minimum_required(VERSION 3.9)
project(NetworksLab2019)

set(CMAKE_CXX_STANDARD 11)
find_package(Curses REQUIRED)
include_directories(${CURSES_INCLUDE_DIR})

add_executable(NetworksLab2019
        task1/client/client.c task1/common/common.c)

target_link_libraries(NetworksLab2019 ${CURSES_LIBRARIES})