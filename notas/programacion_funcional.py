
def saludar(nombre):
    print("Hola "+ nombre + "!")

saludar("Menchu")
saludar("Federico")

nombre = "Fermín"
saludar(nombre)

funcion = saludar   # Cuando el lenguaje me permite que una variable apunte a una función  
funcion("Menchu")   # y posteriormente ejecutar la función desde la variable
                    # entonces decimos que el lenguaje soporta el paradigma funcional.

def imprimir_resultado_de(operacion_a_realizar, numero):
    resultado = operacion_a_realizar(numero)
    print("El resultado es " + str(numero) + " es: " + str(resultado))

def doblar(numero):
    return numero * 2

def triplicar(numero):
    return numero * 3

imprimir_resultado_de(doblar, 5)
imprimir_resultado_de(triplicar, 5)       # En este caso, que función se ejecuta primero? imprimir_resultado_de


#    imprimir_resultado_de(triplicar(5))    En este caso, que función se ejecuta primero? triplicar

# Es decir, con nuestro caso (programación funcional) lo que hemos hecho es parametrizar la operación que debe realizar la función imprimir_resultado_de.
# Dicho de optra forma, inyectar LOGICA a la función imprimir_resultado_de.
# No solo le inyectamos datos... La programación funcional me permite inyectar LOGICA! Y esto es una rayada!

# Hay veces que, cuando creo una función), parte de la lógica de esa función es desconocida... 
# o simplemente no es responsabilidad de esa función conocer esa lógica...
# Entonces, lo que hago es parametrizar esa lógica a través de una función que se le pasa como parámetro a la función que estoy definiendo.
# Esto lo puedo hacer fácilmente a través de la programación funcional.

class ImpresorDeResultados:

    def operacion(self, numero):
        pass

    def imprimir_resultado_de(self, numero):
        resultado = self.operacion(numero)
        print("El resultado es " + str(numero) + " es: " + str(resultado))

class ImpresorDeResultadosDobles(ImpresorDeResultados):

    def operacion(self, numero):
        return numero * 2

class ImpresorDeResultadosTriples(ImpresorDeResultados):

    def operacion(self, numero):
        return numero * 3

impresor = ImpresorDeResultadosDobles()
impresor.imprimir_resultado_de(5)
impresor = ImpresorDeResultadosTriples()
impresor.imprimir_resultado_de(5)


def imprimir_saludo(funcion_generadora_de_saludos, nombre):
    saludo = funcion_generadora_de_saludos(nombre)
    print(saludo)

def generar_saludo_formal(nombre):
    return "Buenos días, " + nombre

def generar_saludo_informal(nombre):
    return "¡Hola " + nombre + "!"

imprimir_saludo(generar_saludo_formal, "Menchu")
imprimir_saludo(generar_saludo_informal, "Menchu")