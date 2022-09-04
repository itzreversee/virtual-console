; simple number operations

; create MyNumbers variable
var MyNumbers
var OtherNumber

; move values into variables
mvv MyNumbers, 10
mvv OtherNumber, 2

; move MyNumbers content into Memory
mva 1, MyNumbers

; move new line character into 4th byte in memory to make a new line when we are printing
mov 4, "\n"

; and Interrupt 0x0A to spit it all!
int 0x0A 1

; add 8 to MyNumbers and print
inc MyNumbers, 8
mva 1, MyNumbers
mov 4, "\n"
int 0x0A 1

; decrement MyNumbers by 2 and print
dec MyNumbers, 2
mva 1, MyNumbers
mov 4, "\n"
int 0x0A 1

; multiply MyNumbers by OtherNumber and print
mul MyNumbers, OtherNumber
mva 1, MyNumbers
mov 4, "\n"
int 0x0A 1

; divide MyNumbers by OtherNumber and print
div MyNumbers, OtherNumber
mva 1, MyNumbers
mov 4, "\n"
int 0x0A 1

flg dbg
dmp Memory
hlt