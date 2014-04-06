package com.example.mtagic;

/* 
 * Indicators.java
 * This enumeration file allows us to easily pass the state of the targets around.
 * As you'll see in PointBoard.java, the BULLSEYE enum will be used to encapsulate
 * the entire target. When the target is hit, the BULLSEYE or MISS enum is sent
 * back to the PointGame, and the approrpriate actions are taken.
 */

public enum Indicators {
BULLSEYE,
MISS;
}
