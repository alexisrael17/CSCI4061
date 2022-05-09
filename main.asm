section	.text
   global _start  ;must be declared for linker (gcc)
	
_start:	          ;tell linker entry point

   mov	edx,len1  ;message length
   mov	ecx,msg1  ;message to write
   mov	ebx,1     ;file descriptor (stdout)
   mov	eax,4     ;system call number (sys_write)
   int	0x80      ;call kernel
	
   mov	edx,len2  ;message length
   mov	ecx,msg2  ;message to write
   mov	ebx,1     ;file descriptor (stdout)
   mov	eax,4     ;system call number (sys_write)
   int	0x80      ;call kernel
  
	
   mov	eax,1     ;system call number (sys_exit)
   int	0x80      ;call kernel
	
section	.data
msg1 db 'Assignment 1 for Alex Lema',0xa  ;a message
len1 equ $ - msg1                         ;length of message

msg2 db 'All done',0xa                    ;a message
len2 equ $ - msg2                         ;length of message