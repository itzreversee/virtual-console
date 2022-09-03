package me.reversee.vconsole.box;

public enum Instructions {
    FLG, // eg. flg dbg                    - sets a flag

    MOV, // eg. mov 1, "Hello, World!"    - moves value into memory
    MVA, // eg. mov 1, PBB                - moves variable into memory
    MVV, // eg. mov 1, hello              - moves variable into memory
    ADD, // eg. add A, 10                 - adds value to variable
    INC, // eg. inc A, 2                  - increments variable by value
    DEC, // eg. dec A, 2                  - decrements variable by value

    LEN, // eg. len A, B                  - sets A's content to be B's length

    VAR, // eg. var hello                 - adds a variable hello

    INT, // eg. int 0x0A 1                - calls an interrupt

    HLT, // eg. hlt                       - halt thread

    DMP, // eg. dmp Memory                - dumps things
}
