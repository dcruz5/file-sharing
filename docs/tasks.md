# File Sharing App - Project

## 1. Desenvolupament del Backend (Servidor)
**Objectiu:** Implementar el servidor amb comunicació segura, gestió de fitxers i control d’usuaris.

### Subtasques del Backend  
#### Configuració del projecte i entorn
- [ ] Configurar el repositori Git i estructura del projecte.
  - Preparar el sistema de compilació i execució (Gradle).
  - Configurar un servidor TLS amb `SSLServerSocket`.

#### Implementació de la comunicació segura (TLS)
- [ ] Creació de certificats (`keystore` i `truststore`).
- [ ] Implementació de `SSLSocket` i `SSLServerSocket` per connexions xifrades.
- [ ] Proves de connexió segura amb un client bàsic.

#### Gestió d’usuaris i autenticació
- [ ] Registre i inici de sessió amb xifrat de contrasenyes (`SHA-256 + sal`).
- [ ] Validació de permisos per accedir a fitxers.
- [ ] Integració amb una base de dades MySQL per gestionar usuaris.

#### Gestió de fitxers (pujada i descàrrega)
- [ ] Implementació de la recepció de fitxers xifrats amb `AES-256`.
- [ ] Emmagatzematge de fitxers al sistema de fitxers o a la BBDD.
- [ ] Implementació del servei de descàrrega amb permisos.

#### Xifratge i desxifratge de fitxers (E2EE)
- [ ] Implementació de `AES-256` per xifrar fitxers abans d’enviar-los.
- [ ] Implementació de `RSA` per xifrar la clau AES.
- [ ] Validació de la seguretat del sistema mitjançant proves.

#### Gestió de permisos i compartició de fitxers
- [ ] Implementació d’un sistema de permisos.
- [ ] Opcional: Sistema de grups d’usuaris.

#### Proves i integració del backend
- [ ] Desenvolupar proves unitàries i d’integració.
- [ ] Verificar la comunicació client-servidor.
- [ ] Depuració d'errors i optimitzacions.

## 2. Desenvolupament del Client
**Objectiu:** Crear un client amb una interfície gràfica per a la interacció amb l’usuari.

### Subtasques del Client  
#### Disseny de la interfície gràfica (JavaFX)
- [ ] Mockups de la interfície (estructura de finestres i botons).
- [ ] Implementació de la finestra principal.

#### Implementació de la comunicació amb el servidor
- [ ] Creació d’un client bàsic amb `SSLSocket`.
- [ ] Enviament de peticions d’autenticació i resposta del servidor.

#### Gestió d'usuaris i autenticació
- [ ] Creació de la interfície d’inici de sessió i registre.
- [ ] Implementació del xifrat de contrasenyes i validació amb el servidor.

#### Implementació de l’enviament i recepció de fitxers
- [ ] Permetre arrossegar i deixar anar (*drag & drop*) fitxers per pujar-los.
- [ ] Implementació del progrés de pujada i descàrrega.

#### Gestió de permisos i compartició de fitxers
- [ ] Interfície per seleccionar qui pot accedir a cada fitxer.
- [ ] Integració amb el servidor per validar permisos.

#### Desxifrat de fitxers al client (E2EE)
- [ ] Implementació del desxifrat `AES-256` després de la descàrrega.
- [ ] Validació que només el destinatari pugui veure el fitxer.

#### Proves finals
- [ ] Fer proves amb diferents tipus d’arxius.
- [ ] Opcional: Millorar l'experiència d’usuari.

