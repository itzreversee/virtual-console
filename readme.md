### virtual console 
version 0.6

My virtual console like app, written in java.

### Usage
You can launch it by using ```java -jar virtual-console.jar``` but it won't do much.  
So you can add parameters to make it do something!  
Here is the list of available parameters:  
 * ```--debug``` used for enabling debug mode. 
 * ```--make-blank``` create sample rom source code file
 * ```--compile-rom-source``` compiles rom source to ```.rtsc``` file (RomTokenizedSourceCode) and ```.rom_cmap```(Rom_CompiledMap) which contains readable to vc Map of Instructions and Parameters like Registers or Values.  
 * ```--source-output``` specifies output ```.rom_cmap``` output location
 * ```--skip-rtsc``` skips writing ```.rtsc``` file 
 * ```--rom``` launches rom inside vc **(Currently can only run ```.rom_cmap``` files!)**
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
```bash
java -jar target/virtual-console.jar --compile-rom-source examples/01-hello.rsc # compile
java -jar target/virtual-console.jar --rom examples/01-hello.rom_cmap # run
```

### Writing code
Check wiki **(not written yet)**

### To-Do
 * [x] Variables
 * [x] Memory management
 * [ ] Functions (like sections in assembly)
 * [x] Input (raw)
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