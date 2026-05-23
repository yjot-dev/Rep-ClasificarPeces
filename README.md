# CLASIFICADOR DE PECES (CP)
CP es una aplicación móvil especializada, diseñada para identificar tres de las especies de peces de acuario más comunes (Guppy, Betta y Molly) a partir de una imagen. El objetivo principal es ofrecer al usuario una herramienta rápida y precisa que, mediante el análisis de la forma y el color en una fotografía, determine la clasificación más probable del pez.

# Características principales
- 🪟 Interfaz clasica con XML
- 📊 Integración con ViewModel + StateFlow
- 🎨 Patrón de diseño arquitectónico con MVVM + Clean Architecture
- 💉 Inyección de dependencias con Hilt
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
El flujo de uso de la aplicación ha sido diseñado para ser directo, eficiente y centrado en la funcionalidad principal, guiando al usuario a través de los siguientes pasos:

1. Inicio e Ingreso: Al abrir la aplicación, el usuario es recibido con una pantalla de bienvenida que lo introduce a la funcionalidad principal. Con un solo toque en el botón "Continuar", accede al núcleo de la herramienta.
2. Selección, Clasificación e Información: Una vez dentro, el usuario interactúa con una vista centralizada que le permite gestionar todo el proceso de identificación en tres acciones clave:
   - Tomar Foto: Activa la cámara del dispositivo para capturar una imagen del pez en tiempo real. La foto se muestra inmediatamente en un visor de imágenes, lista para ser analizada.
   - Seleccionar Foto: Permite al usuario acceder a la galería de su dispositivo para elegir una imagen existente.
   - Clasificar Foto: Con la imagen ya cargada, esta acción inicia el proceso de análisis. La aplicación procesa la fotografía y presenta como resultado la especie identificada (Guppy, Betta o Molly) junto con la probabilidad de acierto de la clasificación.
3. Información del Pez: Una vez obtenido el resultado, el usuario puede dar click en dicho resultado, mostrando una nueva vista con mas informacion del pez.

En resumen, CP es una herramienta práctica y enfocada que aprovecha la cámara y el almacenamiento del dispositivo para ofrecer una solución de clasificación de imágenes simple y efectiva. Combina una arquitectura de software robusta y moderna con una experiencia de usuario intuitiva, permitiendo a cualquier persona, desde aficionados hasta expertos en acuarios, identificar especies de peces de manera rápida y eficiente.

# Ver video Demo
[Ver en Youtube](https://youtu.be/3FDOKiVwNqk)

# Contribución
- Haz un fork del repositorio
- Crea una rama con tu feature: git checkout -b feature/nueva-funcionalidad
- Haz commit de tus cambios: git commit -m "Agrega nueva funcionalidad"
- Haz push a la rama: git push origin feature/nueva-funcionalidad
- Abre un Pull Request

# Licencia
Este proyecto está bajo la licencia GPL-3.0. Consulta el archivo LICENSE para más detalles.