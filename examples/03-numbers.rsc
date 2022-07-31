; simple number operations

; create MyNumbers variable
var MyNumbers

; move 10 into MyNumbers
mvv MyNumbers, 10

; move MyNumbers into PrintBufferA
mva pba, MyNumbers

; move new line character into PrintBufferB to make new line every time we print something
mov pbb, "\n"

; and Interrupt 0x0A to spit it all!
int 0x0A

; add 5 to MyNumbers and print
add MyNumbers, 5
mva pba, MyNumbers
int 0x0A

; decrement MyNumbers by 20 and print
dec MyNumbers, 20
mva pba, MyNumbers
int 0x0A

; increment MyNumbers by 20 and print
inc MyNumbers, 16
mva pba, MyNumbers
int 0x0A