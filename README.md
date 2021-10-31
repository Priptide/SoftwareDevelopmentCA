# SoftwareDevelopmentCA


Requirements: 

- [x] Once a black bag is empty, all the pebbles from a white bag should be emptied into it. Bag X should be filled by bag A, bag Y filled by bag B, and bag Z filled by bag C. 

- [x] The process of drawing pebbles from a black bag should be uniformly at random – that is it should be equally probable to draw any of the pebbles in the bag. 

- [ ] The players should act as concurrent threads, with the threading commencing before drawing their initial pebbles. 

- [x] Players should be implemented as nested classes within a PebbleGame application. 

- [ ] Drawing and discarding should be an atomic action – additionally, the bag a pebble is discarded to, must be the paired white bag of the black bag that the last pebble draw was from. Specifically, if the last pebble was drawn from X, the next discard should be to A, if the last pebble drawn was from Y, the next discard should be to B, and if the last pebble drawn was from Z, the next discard should be to C. 

- [x] On loading the bag files, the program should ensure that each black bag contains at least 11 times as many pebbles as players. For example, if there are three players then there must be at least 33 pebbles. 

- [x] If a player attempts to draw from an empty black bag, the player should attempt to select another bag until they select a bag with pebbles. 
- [x] Pebbles must have a strictly positive weight – therefore the program should detect and warn the user if they are trying to use files where this is not the case.

To-do:

- [ ] Make a variable to check all threads are started before beginning the game.

- [ ] Write test cases for the PebbleGame java file.

- [ ] Make variable to check that all players have made their moves before going onto the next turn.