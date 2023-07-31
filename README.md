![banner_lines_github](https://github.com/git-jr/Threads-Jetpack-Compose/assets/35709152/3a15d03f-8d95-47bc-b4c7-b048b29ab3b6)

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
<img src="https://github.com/git-jr/Threads-Jetpack-Compose/assets/35709152/17a849b8-07ee-40ea-96fb-d8003a399375" alt="preview_1" width="250px" />
<img src="https://github.com/git-jr/Threads-Jetpack-Compose/assets/35709152/5c8c3058-5d19-4dbe-8ad9-e18e8c02f1dd" alt="preview_2" width="250px" />
<img src="https://github.com/git-jr/Threads-Jetpack-Compose/assets/35709152/51469092-9f5d-4a7b-b4b0-fed3243c7eb7" alt="preview_3" width="250px" />
<img src="https://github.com/git-jr/Threads-Jetpack-Compose/assets/35709152/49a3b550-1462-45e1-8e09-d898fa35a333" alt="preview_4" width="250px" />
<img src="https://github.com/git-jr/Threads-Jetpack-Compose/assets/35709152/e56cc81c-d5ff-4c45-9081-33709d9d5c6a" alt="preview_5" width="250px" />
<img src="https://github.com/git-jr/Threads-Jetpack-Compose/assets/35709152/2ab51014-d19f-4287-b218-790b47b021ee" alt="preview_6" width="250px" />

## 🏃‍♂️ Algumas animações
https://github.com/git-jr/Threads-Jetpack-Compose/assets/35709152/62094cc8-6ec0-4164-af3b-7b047a5f732d


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
   
      <img src="https://github.com/git-jr/Threads-Jetpack-Compose/assets/35709152/8d76a6c1-a29e-4b40-a9a4-64f14c569fe0" alt="localizacao-google-services" width="300px" />
   
2. `local.properties`, esse arquivo é gerado automaticamente pelo Android Studio, dentro será necessário adicionar 3 linhas de código para identificar o app perante a API de Login do Facebook
    -  Na [documentação oficial do Facebook][tutorial-facebook-login-api], você encontrará instruções para criar `facebookAppId`, `fbLoginProtocolScheme` e `facebookClientToken`. Depois de obtê-los, adicione cada um desses valores ao arquivo de propriedades em linhas separadas e referenciando seus nomes.

        <img src="https://github.com/git-jr/Threads-Jetpack-Compose/assets/35709152/9540c345-4f09-464e-a151-c554024a55e1" alt="exemplo-arquivo-local-properties" height="150px" />

## 🌟 Conto com você
⭐ Se gostou do projeto não esquece de clicar aí na estrela ⭐

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
