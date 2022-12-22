package com.mic.garage.service.command;

interface CommandService <T>{

    T create(T vehicle);

    void delete(Long id);

    T modify(T vehicle, Long id);


}
