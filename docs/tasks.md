# File Sharing App - Project

## 1. Desenvolupament del Backend (Servidor)
**Objectiu:** Implementar el servidor amb comunicació segura, gestió de fitxers i control d’usuaris.

### Subtasques del Backend  
#### Configuració del projecte i entorn
- [x] Configurar el repositori Git i estructura del projecte.
- [x] Preparar el sistema de compilació i execució (Gradle).
- [x] Configurar un servidor TLS.

#### Implementació de la comunicació segura (TLS)
- [x] Creació de certificats (`keystore` i `truststore`).
- [ ] Implementació de `SSLSocket` i `SSLServerSocket` per connexions xifrades.
- [ ] Proves de connexió segura amb un client bàsic.

#### Gestió d’usuaris i autenticació
- [x] Registre i inici de sessió.
- [ ] Validació de permisos per accedir a fitxers.
- [x] Integració amb una base de dades MySQL per gestionar usuaris.

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

#### Proves i integració del backend
- [x] Desenvolupar proves unitàries.
- [ ] Desenvolupar proves d’integració
- [x] Verificar la comunicació client-servidor.
- [ ] Depuració d'errors i optimitzacions.
- [ ] Fer proves amb diferents tipus d’arxius.

## 2. Desenvolupament del Client
**Objectiu:** Crear un client amb una interfície gràfica per a la interacció amb l’usuari.

### Subtasques del Client  
#### Disseny de la interfície gràfica (JavaFX)
- [X] Mockups de la interfície (estructura de finestres i botons).
- [x] Implementació de la finestra principal.

#### Implementació de la comunicació amb el servidor
- [x] Creació d’un client bàsic amb `SSLSocket`.
- [x] Enviament de peticions d’autenticació i resposta del servidor.

#### Gestió d'usuaris i autenticació
- [x] Creació de la interfície d’inici de sessió i registre.
- [x] Implementació del xifrat de contrasenyes i validació amb el servidor.

#### Implementació de l’enviament i recepció de fitxers
- [x] Permetre arrossegar i deixar anar (*drag & drop*) fitxers per pujar-los.
- [ ] Implementació del progrés de pujada i descàrrega.

#### Gestió de permisos i compartició de fitxers
- [ ] Interfície per seleccionar qui pot accedir a cada fitxer.

#### Desxifrat de fitxers al client (E2EE)
- [ ] Implementació del desxifrat `AES-256` després de la descàrrega.

#### Proves finals
- [ ] Fer proves unitàries.


## Optional tasks (Server & Client)
- [ ] Sistema de grups d’usuaris.
- [ ] Millorar l'experiència d’usuari.

