# Double McHarvard processor

A simulation of a fictional processor using Java. 

### Architecture type

Harvard.

### Memory

- **Instruction memory**: 1024 words where every word's size is 16 bits.
- **Data memory**: 2048 words where every word's size is 8 bits.
- Both memories are word addressable.

### Registers

- **64 General-Purpose Registers**: From R0 to R63. The size of each of them is 8 bits.

- **1 status register**: the status register is a collection of flags that the processor uses. Every bit of the register represents a flag that is set to 1 when the flag is on and 0 when it is off.

  |  7   |  6   |  5   |  4   |  3   |  2   |  1   |  0   |
  | :--: | :--: | :--: | :--: | :--: | :--: | :--: | :--: |
  |  0   |  0   |  0   |  C   |  V   |  N   |  S   |  Z   |

  Here we only need 5 flags so the three most significant bits will always be set to 0.

  - **C flag**: It represents the carry flag. It indicates when an arithmetic carry or borrow has been generated out of the most significant bit position. If there is a carry after the operation the flag is set to 1, else it will be set to 0. The flag is only affected by the addition operation.
  - **V flag**: It represents the two's complement overflow flag. It Indicates when the result of a signed number operation is too large, causing the high-order bit to overflow into the sign bit. In other words, if the sign of the result of the arithmetic operation is different from the expected sign of the result we say that an overflow happened and in this case the flag is set to 1, else it will be set to 0. The V flag is only affected by addition and subtraction operations.
  - **N flag**: It represents the negative flag. It is set to 1 when the result of an instruction that is written back in the register file is negative, else it is set to 0. It is affected by the addition, subtraction, multiplication, and, or and circular shift instructions.
  - **S flag**: It represents the sign flag. This flag is set to 1 if the expected sign of an arithmetic operation (not the actual sign) is negative, else it is et to 0. The S flag is only affected by addition and subtraction operations.
  - **Z flag**: It represents the zero flag. It is set to 1 when the result of an instruction that is written back in the register file is zero, else it is set to 0. It is affected by the addition, subtraction, multiplication, and, or and circular shift instructions.

- **1 Program Counter**: It is a special purpose register whose size is 16 bits. The Program Counter (PC) contains the address of the instruction that is currently being fetched from the instructions memory. After every fetch, the value inside the PC is incremented by 1. The value inside the PC might be changed also while executing branch and jump instructions.

### Instruction Set Architecture

- **Instruction size**: 16 bits.

- **Instruction Types**:

  - R-Format:

    | OPCODE |  R1  |  R2  |
    | :----: | :--: | :--: |
    |   4    |  6   |  6   |

    *The opcode field specifies the instruction name. R1 R2 are the registers' numbers that contains the values that will be used as arguments for the instruction.*

  - I-Format:

    | OPCODE |  R1  | Immediate |
    | :----: | :--: | :-------: |
    |   4    |  6   |     6     |

    *The opcode field specifies the instruction name. R1 is the register's number that contains the values that will be used as the first argument for the instruction, while the immediate is an input specified by the user in the instruction that will be used directly as the second argument.*

- Instructions Count: 12

  |   Format    | Type |                         Description                          |
  | :---------: | :--: | :----------------------------------------------------------: |
  |  ADD R1 R2  |  R   |  Add the content of R1 and R2 and stores the result in R1.   |
  |  SUB R1 R2  |  R   | Subtract the content of R2 from R1 and stores the result in R1. |
  |  MUL R1 R2  |  R   | Multiply the content of R1 and R2 and stores the result in R1. |
  | LDI R1 IMM  |  I   |           Store the value of the immediate in R1.            |
  | BEQZ R1 IMM |  I   | It branches to the instruction that is after it with (IMM) words if R1 contains 0. |
  |  AND R1 R2  |  R   | Performs bitwise AND between the contents of R1 and R2 and stores the result in R1. |
  |  OR R1 R2   |  R   | Performs bitwise OR between the contents of R1 and R2 and stores the result in R1. |
  |  JR R1 R2   |  R   | It stores the content of R1 in the 8 most significant bits of the PC, while R2 in the least significant bits. |
  | SLC R1 IMM  |  I   | Performs left circular shift on the content of R1 (IMM) times. |
  | SRC R1 IMM  |  I   | Performs right circular shift on the content of R1 (IMM) times. |
  |  LB R1 IMM  |  I   | It stores the word at address (IMM) in the data memory into R1. |
  |  SB R1 IMM  |  I   | It stores the content of R1 in the data memory at address (IMM). |


### Data path

- **Stages**: 3

  1. **Fetch**: Fetches the next instruction from the instruction memory using the
     address in the PC (Program Counter), and increments the PC.
  2. **Decode**: Decodes the instruction and reads any operands required from
     the register file.
  3. **Execute**: Performs the logic of the instruction, read and write in the memory if necessary.

- **Pipeline**: 3 instructions are running in the same clock cycle at maximum. One is being fetched, one is being decoded and one is being executed.

  | Cycle |     Fetch     |    Decode     |    Execute    |
  | :---: | :-----------: | :-----------: | :-----------: |
  |   1   | Instruction 1 |               |               |
  |   2   | Instruction 2 | Instruction 1 |               |
  |   3   | Instruction 3 | Instruction 2 | Instruction 1 |
  |   4   | Instruction 4 | Instruction 3 | Instruction 2 |
  |   5   | Instruction 5 | Instruction 4 | Instruction 3 |
  |   6   | Instruction 6 | Instruction 5 | Instruction 4 |
  |   7   | Instruction 7 | Instruction 6 | Instruction 5 |
  |   8   |               | Instruction 7 | Instruction 6 |
  |   9   |               |               | Instruction 7 |

### Hazards

- **Data hazards**: Data hazards occurs when the instruction being decoded reads from the register file, then in the same clock cycle the instruction being executed changes the values in the register file. In this case the values obtained by the decoded instruction are old and in the next cycle it will be executing based on the wrong arguments.

  ###### Solution:

  We could avoid these data hazards by finishing the execution of the executing instruction at the beginning of the clock cycle. In this case, decoding will happen only after execution is finished so the values inside the register files will not be changed untill the next cycle.

-  **Control hazards**: Control hazards occur when we perform branch or jump instructions. When the instruction is executed it changes the value inside the PC to point to the instruction that we wish to execute next. However, there are two instructions in the pipeline that are still being fetched or decoded that will flow to the executing phase in the next two cycles and we do not want this to happen.

  - ###### Solution:

  
  We could avoid these data hazards by finishing the execution of the executing instruction at the beginning of the clock cycle. In this case, decoding will happen only after execution is finished so the values inside the register files will not be changed untill the next cycle.
  
  

### Graphical User Interface

The Graphical user interface is implemented using SWING. It consists of three panels.

![](https://github.com/ziadsadek999/Saturn-Simulator/blob/main/Capture.PNG)

1. Panel 1 is the textbox in which you will type your instructions. Each instruction must be written in a separate line.
2. Panel 2 is the log that in which information about each clock cycle is printed.
3. Panel 3 shows the content the 64 general purpose registers.

After typing your instructions in the first panel, you may click `Next Cycle` or `Run ALL`. Either way, the textbox becomes uneditable after you click and you can not edit it again until execution is finished and you press `reset` to write another code.

If you click on `Run ALL` all your instructions will be executed and the changes in the pipeline, registers and memory will be shown every clock cycle in panel 2.

If you click `Next Cycle` it will show you the next clock cycle only.

### How to run?

You can run the project by accessing the file `CPU.java` inside the `components` package. This file contains the `main` method that will run the project.

### Heroes behind this project

- [Ziad Ahmad Sadek Othman](https://github.com/ziadsadek999)

- [Abdelrahman Fathy Elsalh](https://github.com/abd0123)

- [Aya Ahmed Mohamed Abd-El-Rahman](https://github.com/AyaFayed)

- [Aly Hassan Elsokkary](https://github.com/Elsokkary101)

- [Abanob Kamal Naeim Wahba](https://github.com/abanob-19)

  
