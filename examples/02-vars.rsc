flg dbg                                 ; set debug flag
var MyVariable                          ; create variable "MyVariable"

; move "Hello, World!\n" into MyVariable
mvv MyVariable, "Hello, World!\n"

; move MyVariable into byte 1 of memory
mva 1, MyVariable

int 0x0A 1                              ; spit out memory
dmp Memory                              ; dump memory
hlt                                     ; halt