![banner_lines_github](https://github.com/git-jr/Threads-Jetpack-Compose/assets/35709152/7c6b3484-e87a-4f20-ad93-14239a88927c)

**Lines** é um aplicativo desenvolvido com propósito de se parecer ao máximo com o app [**Threads**][threads-net] da [Meta][meta], feito em Jetpack Composse assim como o original esse projeto também teve o objetivo de ser feito no menor tempo possível, em breve o [resultdo saí aqui](https://www.youtube.com/@JrObom/videos)

💻 As seguintes tecnologias estão em uso no momento:
- [Jetpack Compose][compose] - Interface de usuário
- [Facebook API][login-facebook] - Sistema de Login
- [Firebase Auth][firebase-auth] - Integração com a API de autenticação do Facebook
- [Firebase Firestore][firebase-firestore] - Banco de dado online
- [Firebase Storage][firebase-storage] - Armazenamento de imagens que podem ser enviadas pelo app
- [LottieFiles][lottie] - Animações controladas usando a API oficial do Airbnb
- [Coil][coil] - Carregamento de imagens
- [Jetpack Compose Animations][compose-animations] - Pequenas animações e transições de elementos de layout

📱 As seguintes funções estão disponíveis no momento:
- Login com Facebook, permitindo trocar algumas informações pessoais do perfil.
- Publicar posts únicos e claro as **Threads**
- Visualizar posts únicos feito por outros usuários
- Interagir com animações de movimento feitas através do Jetpack Compose e da API Lottie
- Explorar a telas Feed, Busca, Post, Notificações e Perfil.

## 🎨 Previews
<img src="https://github.com/git-jr/Threads-Jetpack-Compose/assets/35709152/875865db-2b1c-4e4d-9e74-ecc57481dc17" alt="preview_5" width="250px" />
<img src="https://github.com/git-jr/Threads-Jetpack-Compose/assets/35709152/da46f21d-ed60-4468-b167-eb8441f9c86c" alt="preview_6" width="250px" />
<img src="https://github.com/git-jr/Threads-Jetpack-Compose/assets/35709152/6f229ef8-cc51-4879-8030-1623fceb302d" alt="preview_2" width="250px" />
<img src="https://github.com/git-jr/Threads-Jetpack-Compose/assets/35709152/6d006d9a-86ff-4710-9dd1-b1cafac86e3e" alt="preview_3" width="250px" />
<img src="https://github.com/git-jr/Threads-Jetpack-Compose/assets/35709152/53b7578b-66fc-4f42-9a91-c0a495e3e1b4" alt="preview_4" width="250px" />
<img src="https://github.com/git-jr/Threads-Jetpack-Compose/assets/35709152/0da31c37-7124-4448-9caf-89cc2abdd266" alt="preview_1" width="250px" />




## 🏃‍♂️ Algumas animações
https://github.com/git-jr/Threads-Jetpack-Compose/assets/35709152/61c577a5-5b91-40c4-8999-962f793dffb5



## 📲 Testar o app
Aviso: A versão atual deste projeto foi desenvolvida com o objetivo de criar, no menor tempo possível, a versão mais próxima do Threads. Você pode conferir o resultado desse desafio em breve [neste vídeo][video-desafio], então ainda tem muita coisa pra ajustar 😉

Vá até [Releases][releases], baixe o arquivo APK da última versão disponível e escolha a forma login:
> Com Facebook: Talvez apareça uma mensagem de "Permissões ainda não verificadas pelo Facebook", mas é porque o app ainda não foi revisado pela equipe do Facebook; pode prosseguir com segurança.

> Como convidado: Não precisa digitar nenhuma credencial, e seu perfil dentro do app será gerado aleatoriamente com dados de teste.



💻 Como rodar o projeto
Esse projeto precisa de 2 arquivos principais para ser compilado corremente no Android Studio:

1. `google-services.json`, arquivo de configuração do Firebase 
   - Você pode aprender como gerar um [através da documentação oficial][tutorial-firebase]
   - Adicione o arquivo gerado dentro da pasta app:
   
      <img src="https://github.com/git-jr/Threads-Jetpack-Compose/assets/35709152/22df4f85-451d-4b31-b666-a49533393f4d" alt="localizacao-google-services" width="300px" />
   

2. `local.properties`, esse arquivo é gerado automaticamente pelo Android Studio, dentro será necessário adicionar 3 linhas de código para identificar o app perante a API de Login do Facebook
    -  Na [documentação oficial do Facebook][tutorial-facebook-login-api], você encontrará instruções para criar `facebookAppId`, `fbLoginProtocolScheme` e `facebookClientToken`. Depois de obtê-los, adicione cada um desses valores ao arquivo de propriedades em linhas separadas e referenciando seus nomes.

        <img src="https://github.com/git-jr/Threads-Jetpack-Compose/assets/35709152/2cc7d3f2-3526-4466-8398-1810fca437da" alt="exemplo-arquivo-local-properties" height="150px" />

## 😎 Gostou do app?
Clica ali na estrela ⭐ do topo para dar aquela força!

[video-desafio]: https://www.youtube.com/watch?v=wmlcasdadMkj2H70
[compose]: https://developer.android.com/jetpack/compose
[threads-net]: https://www.threads.net/
[meta]: https://about.meta.com/
[login-facebook]: https://developers.facebook.com/docs/facebook-login
[lottie]: https://airbnb.io/lottie

[firebase-auth]: https://firebase.google.com/docs/auth
[firebase-storage]: https://firebase.google.com/docs/storage
[firebase-firestore]: https://firebase.google.com/docs/firestore

[coil]: https://coil-kt.github.io/coil/compose/
[compose-animations]: https://developer.android.com/jetpack/compose/animation

[releases]:https://github.com/git-jr/Threads-Jetpack-Compose/releases

[tutorial-firebase]: https://firebase.google.com/docs/android/setup?hl=pt-br#create-firebase-project
[tutorial-facebook-login-api]: https://developers.facebook.com/docs/facebook-login/android
