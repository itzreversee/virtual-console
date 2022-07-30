package me.reversee.vconsole.box;

public enum _tokenValues {

    None,               // used for non-existing value ( instruction that does not need further values )
    Instruction,        // used for instructions
    Address,            // used for addresses
    AddressB,           // used for addresses ( if multiple in same hashmap )
    Target,             // used for jump targets
    Section,            // used for sections

    ValueAny,           // used for defining unknown value when getting compile set
    ValueString,        // used for strings
    ValueInteger,       // used for integers
    ValueBoolean,       // used for booleans
    ValueList,          // used for lists
    ValueDebugString,   // used for debugging

    HexadecimalAddress, // used for interrupts, flags and other

}
