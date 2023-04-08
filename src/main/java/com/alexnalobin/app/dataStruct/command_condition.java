package com.alexnalobin.app.dataStruct;

public enum command_condition {
    finished,
    started_new_command,
    working,
    waiting_for_input,
    critical_error,
    non_critical_error,
    ended
}
