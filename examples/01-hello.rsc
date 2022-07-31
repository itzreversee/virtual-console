; simplest "Hello, World!" app!

; move string into PrintBufferA
mov pba, "Hello, World!\n"

; use interrupt 0x0A to spit out entire print buffer (a-f)
int 0x0A