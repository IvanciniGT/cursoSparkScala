
DNI. Validación de la Letra.
Cogemos el número y dividimos entre 23... y nos quedamos con el restod e esa división.

23.000.022 | 23
           +----------
        22   1.000.000
        ^^
        Esto es lo que me interesa: Estará en el rango 0-22.
        El ministerio da una tabla con la letra correspondiente a cada número.

Cómo guardo un DNI en una BBDD
- TEXTO(9) <--- Cuánto ocupa eso? Depende del juego de caracteres
                        ASCII
                        ISO-8859-1
                        UTF-8
                        Con cualquiera de esos: 1 caracter básico = 1 byte -> 9 bytes
- NUMERO + LETRA        4 bytes + 1 byte de la letra= 5 bytes
- NUMERO                4 bytes

Es decir, puedo llegar a una reducción del 55% del dato.

El almacenamiento es caro o barato hoy en día? Sigue siendo de las cosas más caras en un entorno de producción.
- Usamos otro tipo de soportes de almacenamiento: x5-x10 en el precio
- En un entorno de pro hacemos al menos 3 copias de cada dato.
  Para tener los 2 tbs, necesito 3x2 discos de 2tbs : x 15-x30
- Y ahora backups: x2.5
Y resulta que al echar cuentas: 1Tb -> 10.000€
Y en casa me cuesta 55 €

---

# MAVEN

Me permite automatizar tareas:
- Compilación
- Ejecución de pruebas
- Generar una imagen docker con mi aplicación
- Desplegar mi aplicación en un servidor de producción
- Mandarla a un repo de git
- Solocitar un análisis a SonarQube
- ...

Maven no sabe hacer la "o" con un canuto!
Cualquier tarea que quiera automatizar quien la ejecuta SIEMPRE es un plugin.
Maven delega la ejecución de tareas a los plugins.

Maven define un ciclo de vida para un proyecto de software. Habla de etapas o GOALS. Esos goals (la mayor parte) son secuenciales:

  validate  -> compile          -> test                 -> package      -> verify -> install
                Compilación        Pruebas unitarias       .jar
                                                            Generar Documentación 

Qué tareas se ejecutan en cada fase/etapa/goal? Depende de mi configuración.
Maven trae una por defecto, simple, muchas veces suficiente. Pero si quiero, puedo definir mi propia configuración, y entonces en cada fase/etapa/goal se ejecutarán las tareas que yo haya definido.
