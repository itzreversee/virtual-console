
var VariableA                          ; create variable "MyVariable"
var VariableB                          ; create variable "MyVariable"

; move Strings
mov 0, "Equal\n"
mov 10, "Not Equal\n"

; move 2 into A
mvv VariableA, 2

; move 2 into B
mvv VariableB, 2

; compare
cmp VariableA, VariableB

; do if equal
deq
int 0x0a 0
enddo

; this will not execute because it is not true
dnq
int 0x0a 10
enddo

; move 4 into B
mvv VariableB, 4

; compare
cmp VariableA, VariableV

; do if not equal
dnq
int 0x0a 10
enddo

flg dbg
dmp Memory
hlt
