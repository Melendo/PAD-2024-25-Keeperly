# Keeperly
Keeperly es un proyecto que busca facilitar la gestión de finanzas personales mediante una aplicación móvil intuitiva y con funciones innovadoras. Keeperly permite al usuario la creación de presupuestos personalizados, clasificarlos en categorías y asociar sus gastos de las cuentas en los distintos presupuestos. De esta forma, permite al usuario visualizar de una forma clara y sencilla el progreso financiero, mostrando cuánto se ha gastado y cuánto queda disponible en cada presupuesto. Esto no solo simplifica el control de las finanzas, sino que también ayuda al usuario a lograr sus objetivos financieros. 

Una de las funcionalidades más importantes de Keeperly es su capacidad para vincularse con tu cuenta de PayPal, lo que permite sincronizar automáticamente  el balance y las transacciones de las cuentas vinculadas. Esta funcionalidad permite eliminar la necesidad de ingresar todos los datos manualmente, garantizando que la información financiera siempre esté actualizada.
## 1. Funcionalidad de Keeperly
Keeperly es una aplicación móvil diseñada para facilitar la gestión de finanzas personales. A continuación, se describen las principales funcionalidades:
- Gestión de usuarios
Registro de usuario: Keeperly permite a los usuarios crear una cuenta proporcionando su nombre, correo electrónico y contraseña. Se implementan validaciones de seguridad, como requisitos de longitud de la contraseña, para garantizar la seguridad de los datos del usuario. 
Inicio y cierre de sesión: los usuarios pueden iniciar sesión con sus credenciales y cerrar sesión manualmente para proteger su información, especialmente en dispositivos compartidos.

- Categorías de gastos
  - Gestión personal: los usuarios pueden crear, modificar y eliminar categorías personalizadas para organizar sus gastos. 
  - Validaciones: permiten asegurar que los nombres de las categorías sean únicos y cumplan un rango de caracteres permitido, para evitar la creación de categorías duplicadas.

- Presupuestos
  - Creación: Keeperly permite la creación de presupuestos asociados a categorías específicas de manera que se puede dividir y estructurar las transacciones en periodos de tiempo que el usuario indique.
  - Modificación: los presupuestos tienen la posibilidad de ser editados o eliminados según sean las necesidades del usuario.
  - Visualización: los presupuestos muestran el estado actual, indicando tanto el importe gastado, como sus fechas de inicio y fin, y la categoría a la que pertenecen.

- Cuentas bancarias
  - Administración: los usuarios pueden crear, editar y eliminar cuentas dentro de la aplicación, registrando el nombre de la cuenta y el balance inicial.
  - Validaciones: permiten asegurar que los nombres de las cuentas cumplan con el rango de caracteres permitidos y el balance inicial sea positivo.
  - Registro financiero: Keeperly permite asociar las transacciones a las cuentas correspondientes.

- Transacciones
  - Registro manual: los usuarios pueden añadir transacciones manualmente, especificando la fecha, categoría, cuenta y monto.
  - Historial: se pueden visualizar todas las transacciones asociadas a una cuenta o categoría, organizadas por orden cronológico.
  - Gestión: Keeperly permite la edición y eliminación de las transacciones existentes que quiera el usuario.

- Sincronización con PayPal
  - Sincronización: Keeperly ofrece la posibilidad de vincular cuentas de PayPal Business, actualizando automáticamente los balances. Esta funcionalidad ayuda a disminuir los registros manuales y asegurar que los datos estén actualizados.


- Modo offline
  - El modo offline garantiza que el usuario pueda registrar gastos y creando presupuesto sin conexión aunque las funcionalidades que tengan que ver con la API de paypal no estén disponibles ya que esto sí que requiere conexión a internet.

## 2. Arquitectura de la aplicación
Keeperly se ha desarrollado con una estructura basada en el patrón MVVM (Model-View-ViewModel). Esta arquitectura permite a la aplicación separar la lógica de la interfaz de usuario de la lógica de negocio y la gestión de datos . 

### 3.1 Model
Este módulo se centra en la gestión de los datos de la aplicación. En nuestro caso, Keeperly almacena sus datos en una base de datos local utilizando Room, una biblioteca de persistencia de datos de Android que permite mapear los datos con POJOs de las entidades. En nuestro proyecto “Model” representa los paquetes de “data” (gestión de datos) y “repository” (interfaz para la obtención de datos de otros módulos)
### 3.2 View
El módulo view representa los componentes encargados de interactuar con el usuario. Para las UI de nuestra aplicación hemos decidido tener 3 activities (registro, login y menú) y el resto de vistas construirlas mediante fragments que hereden el ciclo de vida de la activity de menú. En Keeperly son los paquetes “res” y las clases que extienden a fragment o activity (LoginActivity, InicioFragment…).
### 3.3 ViewModel
El módulo ViewModel actúa como intermediario entre la View y el Model. Su principal función es proporcionar los datos necesarios a la View para que los muestre al usuario, y gestionar la lógica de la UI, como la validación de datos, la navegación y las llamadas a la capa de datos a través de los Repositories. Se definen clases específicas ViewModels construidas a través de factorías que funcionan como singletons para tener una única instancia de estos.
