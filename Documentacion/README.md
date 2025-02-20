# Configuración del Proyecto Blaze

## 1. Configuración Inicial
### Frontend:
- Abrir el proyecto en **Android Studio**.

### Backend:
- Abrir el proyecto en **IntelliJ IDEA** o **NetBeans**.
- Crear la base de datos en MySQL con el nombre: `Blaze`.
  ```sql
  CREATE DATABASE Blaze;
  ```

## 2. Carga Automática de Usuarios
- La carga de usuarios se realiza automáticamente a través del archivo `Config/user_initializer.java` en el backend.

## 3. Configuración de Almacenamiento de Imágenes
- Crear una carpeta para almacenar las fotos de los usuarios.
  ```bash
  mkdir -p /home/emma/pictures/
  chmod +r /home/emma/pictures/
  ```
- Editar el archivo `application.properties` y actualizar la ruta de almacenamiento de las imágenes con la carpeta creada.
- Modificar `UploadFilesServicesImpl` para configurar el nuevo path de la carpeta `pictures`.
- Acceder a `PictureController` y actualizar la ruta en el método `getPhoto`.

## 4. Configuración del Frontend
- Copiar el archivo `keys.xml` en la carpeta `res/values/`.
- Para modificar valores como la dirección IP del servidor o del socket, editar el archivo `strings.xml` dentro del frontend.

## 5. Generación del APK Firmado
- Generar un APK con la firma de desarrollo.
- Se proporciona la carpeta con la firma necesaria para la autenticación con Google.

  
## 6. Notas Finales
- Verificar que todas las configuraciones han sido aplicadas correctamente antes de compilar.
- Pegar las imagenes por default en la carpeta pictures.
- Si hay errores de conexión o permisos, revisar la configuración en `application.properties` y `strings.xml`.

