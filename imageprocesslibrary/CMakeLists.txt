cmake_minimum_required(VERSION 3.4.1)


set(CMAKE_VERBOSE_MAKEFILE on)
set(libs "${CMAKE_SOURCE_DIR}/src/main/libs")
include_directories(${CMAKE_SOURCE_DIR}/src/main/cpp/include)


add_library(libopencv_java3 SHARED IMPORTED)
set_target_properties(libopencv_java3 PROPERTIES
        IMPORTED_LOCATION "${libs}/${ANDROID_ABI}/libopencv_java3.so")

add_library(libopencv_java SHARED IMPORTED )
set_target_properties(libopencv_java PROPERTIES
        IMPORTED_LOCATION "${libs}/${ANDROID_ABI}/libopencv_java.so")

add_library(libface_detection SHARED IMPORTED )
set_target_properties(libface_detection PROPERTIES
        IMPORTED_LOCATION "${libs}/${ANDROID_ABI}/libface_detection.so")

set(CMAKE_CXX_FLAGS "${CMAKE_CXX_FLAGS} -std=gnu++11 -fexceptions -frtti")


add_library(imageprocess SHARED src/main/cpp/imageprocess.cpp)

find_library(log-lib log)


target_link_libraries(imageprocess android log
        libopencv_java3
        libopencv_java
        libface_detection
        ${log-lib}
        )
