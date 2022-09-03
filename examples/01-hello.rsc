; simplest "Hello, World!" app!

; move string into byte 1 of memory
mov 1, "Hello, World!\n"

; use interrupt 0x0A to spit out bytes from memory address 1 until an address that contains byte '-3' in it
int 0x0A 1
