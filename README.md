# CLASIFICADOR DE PECES (CP)
Esta app permite analizar una imagen de tres tipos comunes de peces pequeños de 
acuario obtenida desde la cámara del dispositivo o la galería de fotos, con el 
fin de determinar la probabilidad de que sea un pez: Guppy, Betta o Molly, según 
su forma y color.

# Características principales
- 🪟 Interfaz clasica con XML
- 📊 Integración con ViewModel + StateFlow
- 🎨 Patrón de diseño arquitectónico con MVVM + Hexagonal
- 🧩 Inyección de dependencias con Hilt
- 📱 Compatible con Android 7.0 (API 24) en adelante

# Instalación
- Clona el repositorio: git clone https://github.com/yjot-dev/Rep-ClasificarPeces.git
- Abre el proyecto en Android Studio (Giraffe o superior)
- Sincroniza dependencias con Gradle
- Conecta un dispositivo o emulador y ejecuta la app

# Tecnologías usadas
- Kotlin
- XML
- AndroidX (Lifecycle, Core KTX)
- Material 3

# Uso
- Al abrir la app, se muestra una vista con dos opciones: tomar una foto o 
seleccionar una foto de la galería, y una imagen donde se mostrará la foto elegida.
- Tomar foto, esta opcion permite abrir la camara y tomar una foto del pez, luego
se procesa dicha imagen y se da el resultado como clasificacion.
- Seleccionar foto, esta opcion permite abrir la galeria y seleccioanr una foto de
un pez, luego se procesa dicha imagen y se da el resultado como clasificacion.

# Ver video Demo
No disponible aun

# Contribución
- Haz un fork del repositorio
- Crea una rama con tu feature: git checkout -b feature/nueva-funcionalidad
- Haz commit de tus cambios: git commit -m "Agrega nueva funcionalidad"
- Haz push a la rama: git push origin feature/nueva-funcionalidad
- Abre un Pull Request

# Licencia
Este proyecto está bajo la licencia GPL-3.0. Consulta el archivo LICENSE para más detalles.