; simple number operations

; create MyNumbers variable
var MyNumbers

; move 10 into MyNumbers
mvv MyNumbers, 10

; move MyNumbers content into Memory
mva 1, MyNumbers

; move new line character into 4th byte in memory to make a new line when we are printing
mov 4, "\n"

; and Interrupt 0x0A to spit it all!
int 0x0A 1

; add 5 to MyNumbers and print
add MyNumbers, 5
mva 1, MyNumbers
mov 4, "\n"
int 0x0A 1

; decrement MyNumbers by 20 and print
dec MyNumbers, 20
mva 1, MyNumbers
mov 4, "\n"
int 0x0A 1

; increment MyNumbers by 10 and print
inc MyNumbers, 10
mva 1, MyNumbers
mov 4, "\n"
int 0x0A 1

flg dbg
dmp Memory
hlt