# Telegram chat bot Oracle
En este taller, explorará cómo utilizar SpringBoot para crear microservicios e implementar una aplicación nativa de la nube. Servirá los datos a un Chatbot de Telegram y los conservará en la base de datos autónoma de Oracle mientras utiliza los servicios de Oracle Cloud Infrastructure (OCI).

### Arquitectura
![image](https://user-images.githubusercontent.com/7783295/116454396-cbfb7a00-a814-11eb-8196-ba2113858e8b.png)
  

# Manual de instlacion

## Configurar el entorno de desarrollo

### log en oracle cloud




### Creacion de un grupo



### Agregar usuarios a tu grupo


### Asignar politicas al compartimentos

* Allow group myToDoGroup to use cloud-shell in tenancy
* Allow group myToDoGroup to manage users in tenancy
* Allow group myToDoGroup to manage all-resources in tenancy
* Allow group myToDoGroup to manage buckets in tenancy
* Allow group myToDoGroup to manage objects in tenancy

### Cloud shell

#### Crear una carpeta para tu Workshop Code
mkdir *reacttodo*__
cd reacttodo

##### Clonar el repositorio
* gir clone https://github.com/Jaredandres/CHAT_BOT_ORACLE.git

### Iniciar la confirugaracion
cd oci-react-samples/MtdrSpring
chmod +x *.sh
echo source $(pwd)/env.sh >> ~/.bashrc
source env.sh
source setup.sh

*Si realizas los cambios correctamente, te solicitaran tu OCID*__

*Este lo puedes encontrar en tu perfil*__

La configuración le pedirá que ingrese la contraseña de administrador de la base de datos. Las contraseñas de las bases de datos deben tener entre 12 y 30 caracteres y contener al menos una letra mayúscula, una letra minúscula y un número. La contraseña no puede tener el carácter de comillas dobles (") ni la palabra "admin".


La configuración le pedirá que cree un nombre de usuario de UI y una contraseña. Lo necesitará para acceder a la aplicación.


## Frontend (Telegram)

Crear un chat bot con la ayuda de *BotFather*__ y optener el token de tu propio bot.

cd $MTDRWORKSHOP_LOCATION/backend/src/main/resources
vi application.properties

![image](https://github.com/Jaredandres/CHAT_BOT_ORACLE/assets/88905921/c81fd787-c449-410c-a3a4-c9665454e1f8)


### Requirements
The lab executes scripts that require the following software to run properly: (These are already installed on and included with the OCI Cloud Shell)
* oci-cli
* python 2.7^
* terraform
* kubectl
* mvn (maven) 

## Expect more ...
