
var InputData             ; create variable "MyInput"
var InputLength         ; create variable "InputLength"

; move string to memory and print
mov 0, "Write something and it will echo!"
int 0x0A 0
int 0xFF

; ask for input and put it into MyInput variable
int 0x1A InputData

; move variable's content into memory
mva 0, InputData

; get length of input
len InputLength, InputData

; move end of string into expected line
mov $InputLength, "\n"

; print it out!
int 0x0A 0

flg dbg     ; set debug flag
dmp Memory  ; dump memory
hlt         ; halt
