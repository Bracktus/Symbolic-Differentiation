# todo 

- fix first rule in binaryMinusRules
- make simplifcationHelper, which runs simplify in a loop until no changes
  are made.
- remove TokenList
- add simplification rules
- to latex method
- slap a gui onto the system
- add method to evaluate the function at a certain value

# rules
- +x = x
  

- x + 0 = x
- x + -x = x - x
- x + +x = x + x
- +x + +x = x + x


- x - 0 = x
- 0 - x = -x
- x - x = 0
- x - -x = x + x
  

- x * 0 = 0
- x * 1 = x
- -x * -x = x * x
  

- x / x = 1
- x / 1 = x
- 0 / x = 0

# ideas

- maybe we need to make the Operation interface have 5 public methods
- generalise the visit method to make it the same every time.
- make the visitor implementations different