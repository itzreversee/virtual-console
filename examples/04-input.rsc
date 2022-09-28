
var MyInput             ; create variable "MyInput"
var InputLength         ; create variable "InputLength"

; move a "Hello" string here
mov 1, "Hello, "

; ask for input and put it into MyInput variable
int 0x1A MyInput

; move variable's content into memory
mva 8, MyInput

; get length of input
len InputLength, MyInput

; increment by 9 to the variable to point it to the right character
inc InputLength, 9

; move end of string into expected line
mov $InputLength, "!\n"

; print it out!
int 0x0A 0

flg dbg     ; set debug flag
dmp Memory  ; dump memory
hlt         ; halt
