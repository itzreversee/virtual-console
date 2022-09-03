; simplest "Hello, World!" app!

; move string into PrintBufferA
mov 1, "Hello, World!\n"

; use interrupt 0x0A to spit out entire print buffer (a-f) int 0x0A
int 0x0A 1
