#Forles inf2440 24012014

##synchronized
I _samme_ objekt!

#Kompleksitetsklasser:
se bigO.
O(n): Usotert liste, sortere denne.
O(nXlogn): quicksort i beste fall
O(n^1,5): multiplisere to matriser med hverandre. (kvadratroten av n på begge sider) - boble, inneffektivt hvis det er veldig mange elementer.
O(n^2):
NP: Eksponensielle. (Travelling salesman - n fakultet - vokser eksponensielt)


Hvis kjøretiden er > 0,2 sekunder vil den paralelle sannsynligvis ta igjen.

## Synkronisert avslutning
## join()-avslutning
## Hvor mye tid bruker parallelle programer
## 'Lover' om kjøretid
    - Amdahls lov
    - Gustafsons lov
## CyclicBarrier
    "Bom"

##Semaphore (sf = Semaphore(-n+1))
    "Tillatelser"
    start med negativt antall billetter.
    sf.aquire() - be om billeltt.
    sf.release() - gi fra seg billett.
    - maintråden sier sf.aquire() og må vente på at det er minst en tillatelse i
      sf. 
    - avslutning. sf.release hos alle => en ledig, main kan kjøre.

##join()
    Venter main til alle tråder main har pekere til terminerer. (må sjekke mot
    disse i f.eks. array).
    "Enklest?"

##Atomic
java.util.concurrent.atomic
###AtomicIntegerArray
Relativt raskt og relativt trygt med delte integer.
Raskere en synchronized.


##Vi lærer:
new Thread - join()
synchronized method (treg)
semaphore
cyclicbarrier - await
ExecutorService pool = Executors.newFixedThreadPool(k);
    med Futures (gjenbruk av tråder)
AtomicIntegerArray - get(), set(), getAndAdd()
volatile variable

##JIT-kompilering
just-in-time (konsekvenser for kjøretiden)
java optimaliserer kode under kjøring.
##Operativsystem
Forskjeller i operativsystemene i efficiency
##Søppeltømming i java
Fjerning av ubrukte objekter

