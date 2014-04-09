#Forelesning 31.jan 2014
##Presisert pensum
##I virkelig liv
- En test på hvor stort problemet er, slik at den ikke bruker parallellitet når
  problemet er raskere å løse sekvensielt
- Kjører Array.sort flere tråder (med grense (limit) 47)?
##Regler som gjør at programmet blir riktig
- 8 relger på foil for uke3 (side 8og9) (Se på disse!!).
- La trådene løse _hele_ problemt.
    - Hvis ett av stegene må være sekvensielt, lar vi tråd med index == 0 gjøre
      det.
- Regler for lesing og skriving av felles data (foil 10) (Se på disse!!).

- Ikke les fra en variabel som noen andre ikke er ferdig med å skrive til:
  double: består av en øvre og nedre del (64 bit, 2x 32bit minneplasser). Hvis
  man leser en variabel som en tråd bare er ferdig med å skrive i den en delen
  kan man få ut verdier som aldri har eksistert annet enn som en sammensetning
  av starten og slutten i en double.
- _Ikke les_ fra en variabel før _synkronisering_.

- Skriving i ulike nærliggende elementer samtidig i en array går bra.
    (men dette kan gå utover lesingen av samme element i en annen tråd enn den
    som skrev (på grunn av cachelinjen (64 bit plasser, int er 4)).
- 10% langsommere å aksessere nabo-elementer i en array (som er på samme
  cache-linje, hver 64 byte) (når det er forskjellige tråder som leser. Hvis det
  er den samme tråden som leser sekvensielt vil det gå raskere pga cache).
- "cache-miss" (eksempel fra f. 25).
- f. 27 (størrelse på variabel har noe å si, cache = 64bit, så vanskelig å
  samarbeide med cache med store variable.
- f. 28. radix sort






- Parallellisere oppstarten (lage halvparten av threads i neste tråd?)
- Tegne sphere med parallellitet?
