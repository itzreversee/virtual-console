### virtual console 
version 0.2  

This is an early version of my virtual console written in java.  
You can write simple code in its own language inspired by assembly.  

### usage
You can launch it by using ```java -jar virtual-console.jar``` but it won't do much.  
So you can add parameters to make it do something!  
Here is the list of available parameters:  
 * ```--debug``` used for enabling debug mode. 
 * ```--make-blank``` create sample rom source code file
 * ```--compile-rom-source``` compiles rom source to ```.rtsc``` file (RomTokenizedSourceCode) and ```.rom_cmap```(Rom_CompiledMap) which contains readable to vc Map of Instructions and Parameters like Registers or Values.  
 * ```--rom``` launches rom inside vc **(not implemented yet)**
 * ```--clear-cache``` clears cache **(not implemented yet)**
 * ```--clear-logs``` clears logs **(not implemented yet)**

### Compiling 
Download source code ( with modules if I add them ), you can use:   
```git clone https://github.com/reversee-dev/virtual-console --recurse-submodules```

After cloning, cd into root dir of source code, and run ```mvn clean package```  
When it finishes, you can run it from ```target/``` folder.  

### Writing code
