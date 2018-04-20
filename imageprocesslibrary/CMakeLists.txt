cmake_minimum_required(VERSION 3.4.1)


add_library( imageprocess
             SHARED
             src/main/cpp/imageprocess.cpp
             )

find_library( log-lib
              log )

target_link_libraries( imageprocess
                       ${log-lib} )
