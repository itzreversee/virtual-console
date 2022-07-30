### virtual console 
version 0.3  

My virtual console like app, written in java.

### Usage
You can launch it by using ```java -jar virtual-console.jar``` but it won't do much.  
So you can add parameters to make it do something!  
Here is the list of available parameters:  
 * ```--debug``` used for enabling debug mode. 
 * ```--make-blank``` create sample rom source code file
 * ```--compile-rom-source``` compiles rom source to ```.rtsc``` file (RomTokenizedSourceCode) and ```.rom_cmap```(Rom_CompiledMap) which contains readable to vc Map of Instructions and Parameters like Registers or Values.  
 * ```--source-output``` specifies output ```.rom_cmap``` output location
 * ```--skip-rsc``` skips writing ```.rsc``` file 
 * ```--rom``` launches rom inside vc 
 * ```--clear-cache``` clears cache **(not implemented yet)**
 * ```--clear-logs``` clears logs **(not implemented yet)**

### Compiling 
You need maven, git and jdk 18 installed for this
If you have these, open terminal and paste this

``` bash
git clone https://github.com/reversee-dev/virtual-console --recurse-submodules # clone source code
cd virtual-console/ # cd into source code directory 
mvn clean package # build 
```

Or if you have IntelliJ idea installed:
* Click build from top menu 
* Click **Build Artifacts...**
* Click **virtual-console:jar** > **Build**

When it finishes, you can run it from ```target/``` (Maven) or ```out/artifacts/``` (IntelliJ) folder:  
```java -jar virtual-console.jar --rom <file.rom_cmap>```

### Writing code
Check wiki **(not written yet)**

### To-Do
 * [ ] Variables
 * [ ] Functions (like sections in assembly)
 * [ ] Input (raw)
 * [ ] Input (key listener)
 * [ ] If else (cmp -> var/reg **or** je) 
 * [ ] Arrays
 * [ ] Compiling to "binary"
 * [ ] ...
 * [ ] "BIOS"
 * [ ] "Operating System"
 * [ ] ROM "Installation"
 * [ ] ROM Saves
 * [ ] ROM Cache
 * [ ] ...
 * [ ] OpenGL
 * [ ] "OS" with GUI
 * [ ] ...
 * [ ] Security