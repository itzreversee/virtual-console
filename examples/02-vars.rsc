flg dbg                                 ; set debug flag
var MyVariable                          ; create variable "MyVariable"

; move "Hello, World!\n" into MyVariable
mvv MyVariable, "Hello, World!\n"

; move MyVariable into PBA
mva PBA, MyVariable

int 0x0A                                ; spit out print buffer
dmp Memory                              ; dump memory
hlt                                     ; halt