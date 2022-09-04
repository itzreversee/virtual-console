package me.reversee.vconsole.box;

public enum Instructions {
    FLG, // eg. flg dbg                    - sets a flag

    MOV, // eg. mov 1, "Hello, World!"    - moves value into memory
    MVA, // eg. mov 1, PBB                - moves variable into memory
    MVV, // eg. mov 1, hello              - moves variable into memory
    ADD, // eg. add A, B                  - adds variable to variable
    INC, // eg. inc A, 2                  - increments variable by value
    DEC, // eg. dec A, 2                  - decrements variable by value
    MUL, // eg. mul A, B                  - multiplies variable by variable
    DIV, // eg. div A, B                  - divides variable by variable
    LEN, // eg. len A, B                  - sets A's content to be B's length
    CMP, // eg. cmp A, B                  - compare 2 variables
    DEQ,// eg. deq                        - do if cmp equal
    DNQ, // eg. dnq                       - do if cmp not equal
    ENDDO,  // eg. enddo                  - end do block
    LOOP,   // eg. loop 5                 - loop x times ( -1 for infinity )
    ENDLOOP,// eg. endloop                - specifies loop ending point
    BREAKLOOP, // eg. breakloop           - breaks loop

    VAR, // eg. var hello                 - adds a variable hello

    INT, // eg. int 0x0A 1                - calls an interrupt

    HLT, // eg. hlt                       - halt thread

    DMP, // eg. dmp Memory                - dumps things
}
