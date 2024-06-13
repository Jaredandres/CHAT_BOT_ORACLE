# Telegram chat bot Oracle
En este taller, explorará cómo utilizar SpringBoot para crear microservicios e implementar una aplicación nativa de la nube. Servirá los datos a un Chatbot de Telegram y los conservará en la base de datos autónoma de Oracle mientras utiliza los servicios de Oracle Cloud Infrastructure (OCI).

### Arquitectura
![image](https://github.com/Jaredandres/CHAT_BOT_ORACLE/assets/88905921/d18a5dcf-2db3-4315-b0d7-83af5cfbfdcb)


# Manual de instalación

## Configurar el entorno de desarrollo

### log en oracle cloud
![log](https://github.com/Jaredandres/CHAT_BOT_ORACLE/assets/88905921/d818142c-cc1e-4dbe-a282-8c27d33dbcd7)


### Creación de un grupo
![image](https://github.com/Jaredandres/CHAT_BOT_ORACLE/assets/88905921/16c62fb0-2bda-4e84-87ed-686915d6e321)
![image](https://github.com/Jaredandres/CHAT_BOT_ORACLE/assets/88905921/1c2bbae6-e608-42d6-a997-d18617dab228)


### Agregar usuarios a tu grupo
![image](https://github.com/Jaredandres/CHAT_BOT_ORACLE/assets/88905921/06b09244-d504-45d9-8927-69109c131c45)


### Asignar políticas al compartimentos
![image](https://github.com/Jaredandres/CHAT_BOT_ORACLE/assets/88905921/f8eb34fe-39dd-4174-ad18-91c2114cab09)

![image](https://github.com/Jaredandres/CHAT_BOT_ORACLE/assets/88905921/b41122c6-e048-45ef-bb5a-ef994f1a97c0)

* Allow group myToDoGroup to use cloud-shell in tenancy
* Allow group myToDoGroup to manage users in tenancy
* Allow group myToDoGroup to manage all-resources in tenancy
* Allow group myToDoGroup to manage buckets in tenancy
* Allow group myToDoGroup to manage objects in tenancy

### Cloud shell
![shell](https://github.com/Jaredandres/CHAT_BOT_ORACLE/assets/88905921/97285e7b-45f9-446f-a7ea-e07809ecfdf3)

#### Crear una carpeta para tu Workshop Code
* mkdir __*reacttodo*__
* cd reacttodo

##### Clonar el repositorio
* gir clone https://github.com/Jaredandres/CHAT_BOT_ORACLE.git

### Iniciar la configuración
* cd oci-react-samples/MtdrSpring
* chmod +x *.sh
* echo source $(pwd)/env.sh  ~/.bashrc
* source env.sh
* source setup.sh

__*Si realizas los cambios correctamente, te solicitaran tu OCID*__
![OCID](https://github.com/Jaredandres/CHAT_BOT_ORACLE/assets/88905921/4d536298-6f5b-4ef8-a332-f644813cadd2)

__*Este lo puedes encontrar en tu perfil*__
![PerfilOCID](https://github.com/Jaredandres/CHAT_BOT_ORACLE/assets/88905921/3380047a-cc39-4c07-8050-e9c9c392c540)

La configuración le pedirá que ingrese la contraseña de administrador de la base de datos. Las contraseñas de las bases de datos deben tener entre 12 y 30 caracteres y contener al menos una letra mayúscula, una letra minúscula y un número. La contraseña no puede tener el carácter de comillas dobles (") ni la palabra "admin".

![DATA](https://github.com/Jaredandres/CHAT_BOT_ORACLE/assets/88905921/dd968ebc-2e28-47ca-a671-719430eacfb4)

La configuración le pedirá que cree un nombre de usuario de UI y una contraseña. Lo necesitará para acceder a la aplicación.

![UI](https://github.com/Jaredandres/CHAT_BOT_ORACLE/assets/88905921/730b5a98-9b10-4c0e-b01d-ac22d0f8f538)



## Frontend (Telegram)
Crear un chat bot con la ayuda de __*BotFather*__ y obtener el token de tu propio bot.

![image](https://github.com/Jaredandres/CHAT_BOT_ORACLE/assets/88905921/8d63c76a-b1d6-4a36-8b3a-e3866f46622e)

* cd $MTDRWORKSHOP_LOCATION/backend/src/main/resources
* vi application.properties

![image](https://github.com/Jaredandres/CHAT_BOT_ORACLE/assets/88905921/c81fd787-c449-410c-a3a4-c9665454e1f8)

## Backend (Java / Spring Boot)

### Cree y envíe las imágenes de Docker al registro OCI
* cd $MTDRWORKSHOP_LOCATION/backend
* source build.sh

![image](https://github.com/Jaredandres/CHAT_BOT_ORACLE/assets/88905921/0904938f-68ce-4cfc-814f-1a85a2db4e7b)


### Implementar en Kubernetes y verificar el estado
* cd $MTDRWORKSHOP_LOCATION/backend
* ./deploy.sh

![image](https://github.com/Jaredandres/CHAT_BOT_ORACLE/assets/88905921/8a3d5ce2-e8f9-428b-bdcf-ef479247a9f2)

* service
![image](https://github.com/Jaredandres/CHAT_BOT_ORACLE/assets/88905921/f9e0bb47-e226-4c8e-9ebe-f6279628a264)

* pods
![image](https://github.com/Jaredandres/CHAT_BOT_ORACLE/assets/88905921/5c3dee17-8066-49e5-be95-ff9303525c58)

Una vez que sus pods estén en funcionamiento. Vaya a su navegador web y navegue hasta la dirección IP del load balancer . Aparecerá la pantalla de inicio de sesión de la aplicación.

![image](https://github.com/Jaredandres/CHAT_BOT_ORACLE/assets/88905921/70781604-d235-4a39-ba12-24fcfe8a7dc1)


Una vez que inicie sesión, debería ver el siguiente resultado, lo que significa que su implementación fue exitosa.

![image](https://github.com/Jaredandres/CHAT_BOT_ORACLE/assets/88905921/306d9d2f-fb06-4cb4-ade1-ef1c80d12372)
