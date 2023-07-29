package com.paradoxo.threadscompose

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.compose.animation.animateColor
import androidx.compose.animation.core.Transition
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.updateTransition
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.gestures.AnchoredDraggableState
import androidx.compose.foundation.gestures.DraggableAnchors
import androidx.compose.foundation.gestures.Orientation
import androidx.compose.foundation.gestures.anchoredDraggable
import androidx.compose.foundation.gestures.animateTo
import androidx.compose.foundation.gestures.animateZoomBy
import androidx.compose.foundation.gestures.awaitEachGesture
import androidx.compose.foundation.gestures.awaitFirstDown
import androidx.compose.foundation.gestures.awaitVerticalDragOrCancellation
import androidx.compose.foundation.gestures.awaitVerticalTouchSlopOrCancellation
import androidx.compose.foundation.gestures.detectTapGestures
import androidx.compose.foundation.gestures.detectVerticalDragGestures
import androidx.compose.foundation.gestures.draggable
import androidx.compose.foundation.gestures.rememberDraggableState
import androidx.compose.foundation.gestures.rememberTransformableState
import androidx.compose.foundation.gestures.transformable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.requiredSize
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clipToBounds
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.input.pointer.positionChange
import androidx.compose.ui.layout.onSizeChanged
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalDensity
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.core.view.WindowCompat
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavDestination
import androidx.navigation.NavDestination.Companion.hierarchy
import androidx.navigation.NavGraph.Companion.findStartDestination
import androidx.navigation.NavGraphBuilder
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navigation
import coil.compose.AsyncImage
import com.facebook.Profile
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase
import com.paradoxo.threadscompose.model.Post
import com.paradoxo.threadscompose.model.UserAccount
import com.paradoxo.threadscompose.network.firebase.PostFirestore
import com.paradoxo.threadscompose.sampleData.SampleData
import com.paradoxo.threadscompose.ui.FeedScreen
import com.paradoxo.threadscompose.ui.NotificationsScreen
import com.paradoxo.threadscompose.ui.PostScreen
import com.paradoxo.threadscompose.ui.SearchScreen
import com.paradoxo.threadscompose.ui.login.AppState
import com.paradoxo.threadscompose.ui.login.LoginScreen
import com.paradoxo.threadscompose.ui.login.LoginViewModel
import com.paradoxo.threadscompose.ui.profile.ProfileEditScreen
import com.paradoxo.threadscompose.ui.profile.ProfileScreen
import com.paradoxo.threadscompose.ui.theme.ThreadsComposeTheme
import com.paradoxo.threadscompose.utils.showMessage
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlin.math.max
import kotlin.math.roundToInt


class MainActivity : ComponentActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        WindowCompat.setDecorFitsSystemWindows(window, false)

        val testMode = true

        if (testMode) {
            setContent {
                Box(
                    Modifier.fillMaxSize(),
                    contentAlignment = Alignment.Center
                ) {

                    Box {
                        VerticalDraggableSample(
                            modifier = Modifier
                                .fillMaxHeight()
                                .align(Alignment.Center)
                        )
                    }

//                GestureAnimationExample()
//                    DraggableText()
//                    TestAnim1()
//                    TestAnim2()
//                    TestAnim3()

//                    TestDragAndDrop()

//                    GestureAnimationExampleOk()
                }
            }
        } else {
            setContent {
                ThreadsComposeTheme {
                    Box(
                        Modifier.fillMaxSize()
                    ) {
                        val navController: NavHostController = rememberNavController()

                        var showNavigationBar by remember { mutableStateOf(false) }
                        val navBackStackEntry by navController.currentBackStackEntryAsState()
                        val currentDestination = navBackStackEntry?.destination

                        val destinysWithoutNavigationBar by remember {
                            mutableStateOf(
                                listOf(
                                    Destiny.Login.route,
                                    Destiny.ProfileEdit.route,
                                    Destiny.Post.route,
                                )
                            )
                        }

                        LaunchedEffect(currentDestination) {
                            showNavigationBar =
                                !destinysWithoutNavigationBar.contains(currentDestination?.route)
                        }

                        ThreadsApp(
                            navController = navController,
                            showNavigationBar = showNavigationBar,
                            currentDestination = currentDestination,
                            content = {
                                ThreadsNavHost(
                                    navController = navController,
                                    navigateToInstagram = {
                                        val instagramIntent = Intent(
                                            Intent.ACTION_VIEW,
                                            Uri.parse("https://www.instagram.com/threadsapp/")
                                        )
                                        startActivity(instagramIntent)
                                    }
                                )
                            }
                        )
                    }
                }
            }
        }
    }

    @Composable
    fun ThreadsApp(
        content: @Composable (PaddingValues) -> Unit,
        navController: NavHostController,
        showNavigationBar: Boolean,
        currentDestination: NavDestination?
    ) {
        Scaffold(
            bottomBar = {
                if (showNavigationBar) {
                    NavigationBar(
                        containerColor = Color.White,
                    ) {
                        screenItems.forEach { screen ->
                            val isSelected =
                                currentDestination?.hierarchy?.any { it.route == screen.route } == true

                            NavigationBarItem(
                                icon = {
                                    screen.resourceId?.let { assetIcon ->
                                        val icon = painterResource(
                                            id = if (isSelected) {
                                                assetIcon.first
                                            } else {
                                                assetIcon.second
                                            },
                                        )


                                        Icon(
                                            icon,
                                            contentDescription = null,
                                            modifier = Modifier
                                                .alpha(
                                                    if (isSelected) 1f else 0.5f
                                                )
                                        )
                                    }
                                },
                                selected = isSelected,
                                onClick = {
                                    if (screen.route == Destiny.Post.route) {
                                        navController.navigate(screen.route) {
                                            launchSingleTop = true
                                        }
                                    } else {
                                        navController.navigate(screen.route) {
                                            popUpTo(navController.graph.findStartDestination().id) {
                                                saveState = true
                                            }

                                            launchSingleTop = true
                                            restoreState = true
                                        }
                                    }
                                },
                                colors = NavigationBarItemDefaults.colors(
                                    indicatorColor = Color.White
                                )
                            )
                        }
                    }
                }
            }
        ) { paddingValues ->
            Column(
                Modifier
                    .fillMaxSize(),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center
            ) {
                Box(Modifier.fillMaxSize()) {
                    content(paddingValues)
                }
            }
        }
    }

    @Composable
    fun ThreadsNavHost(
        navController: NavHostController,
        navigateToInstagram: () -> Unit = {}
    ) {
        val sessionViewModel: SessionViewModel = viewModel()
        val state = sessionViewModel.uiState.collectAsState()

        NavHost(
            navController = navController,
            startDestination = Destiny.LoginNavigation.route
        ) {
            loginGraph(
                onNavigateToHome = { currentUser ->
                    sessionViewModel.serCurrent(currentUser)

                    navController.navigate(Destiny.HomeNavigation.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }
                        launchSingleTop = true
                        restoreState = true
                    }
                },
                onNavigateToProfileEdit = {
                    navController.navigate(Destiny.ProfileEdit.route) {
                        popUpTo(navController.graph.findStartDestination().id) {
                            saveState = true
                        }

                        launchSingleTop = true
                        restoreState = true
                    }
                }
            )
            homeGraph(
                state = state,
                onNavigateToInstagram = {
                    navigateToInstagram()
                },
                onBack = {
                    navController.popBackStack()
                }
            )
        }
    }

    private fun NavGraphBuilder.loginGraph(
        onNavigateToHome: (UserAccount) -> Unit = {},
        onNavigateToProfileEdit: () -> Unit = {}
    ) {
        navigation(
            startDestination = Destiny.Login.route,
            route = Destiny.LoginNavigation.route
        ) {
            composable(Destiny.Login.route) {
                val loginViewModel: LoginViewModel = viewModel()

                LoginScreen(
                    loginViewModel = loginViewModel,
                    onAuthComplete = {
                        it?.let {
                            loginViewModel.getProfile(
                                onSuccess = { userAccountOnFirebase ->
                                    if (userAccountOnFirebase.userName.isNotEmpty()) {
                                        onNavigateToHome(userAccountOnFirebase)
                                    } else {
                                        onNavigateToProfileEdit()
                                    }
                                },
                                onError = {
                                    onNavigateToProfileEdit()
                                }
                            )
                        } ?: run {
                            onNavigateToHome(SampleData().generateSampleInvitedUser())
                        }
                    },
                )
            }

            composable(Destiny.ProfileEdit.route) {
                val loginViewModel: LoginViewModel = viewModel()
                val context = LocalContext.current

                val currentUser = Firebase.auth.currentUser

                val profile = Profile.getCurrentProfile()
                val profilePicUri = profile?.getProfilePictureUri(200, 200)

                val userAccount = UserAccount(
                    id = currentUser?.uid ?: "",
                    name = currentUser?.displayName ?: "",
                    userName = currentUser?.displayName?.lowercase()?.replace(" ", "_") ?: "",
                    imageProfileUrl = profilePicUri?.toString() ?: "",
                )

                ProfileEditScreen(
                    userAccount = userAccount,
                    onSave = { userAccountUpdated ->
                        loginViewModel.saveNewUser(
                            userAccount = userAccountUpdated,
                            onSuccess = {
                                onNavigateToHome(userAccountUpdated)
                            },
                            onError = {
                                context.showMessage("Erro ao salvar informações do perfil")
                            }
                        )
                    }
                )
            }
        }
    }

    private fun NavGraphBuilder.homeGraph(
        state: State<SessionState>,
        onNavigateToInstagram: () -> Unit = {},
        onBack: () -> Unit = {},
        paddingValues: PaddingValues = PaddingValues(0.dp)
    ) {
        navigation(
            startDestination = Destiny.Feed.route,
            route = Destiny.HomeNavigation.route
        ) {
            composable(Destiny.Feed.route) {
                val postViewModel: FeedViewModel = viewModel()
                val postState by postViewModel.uiState.collectAsState()

                FeedScreen(posts = postState.posts)
            }
            composable(Destiny.Search.route) { SearchScreen() }
            composable(Destiny.Post.route) {

                val scope = rememberCoroutineScope()

                PostScreen(
                    currentUser = state.value.userAccount,
                    onBack = {
                        onBack()
                    },
                    onSendPost = { posts ->
                        val postFirestore = PostFirestore()
                        scope.launch {
                            postFirestore.insertPost(
                                posts = posts,
                                onSuccess = {
                                    onBack()
                                },
                                onError = {
                                    onBack()
                                }
                            )
                        }

                    }
                )
            }
            composable(Destiny.Notifications.route) { NotificationsScreen() }
            composable(Destiny.Profile.route) {
                ProfileScreen(
                    currentUser = state.value.userAccount,
                    modifier = Modifier.padding(paddingValues),
                    onNavigateToInstagram = {
                        onNavigateToInstagram()
                    }
                )
            }
        }
    }

    sealed class Destiny(val route: String, val resourceId: Pair<Int, Int>? = null) {
        object Login : Destiny("login")
        object Feed : Destiny("feed", Pair(R.drawable.ic_home, R.drawable.ic_home_outlined))
        object Search : Destiny("search", Pair(R.drawable.ic_search, R.drawable.ic_search))
        object Post : Destiny("post", Pair(R.drawable.ic_post, R.drawable.ic_post))
        object Notifications :
            Destiny("notifications", Pair(R.drawable.ic_heart, R.drawable.ic_heart_outlined))

        object Profile :
            Destiny("profile", Pair(R.drawable.ic_profile, R.drawable.ic_profile_outlined))

        object ProfileEdit : Destiny("profileEdit")

        object LoginNavigation : Destiny("loginNavigation")
        object HomeNavigation : Destiny("homeNavigation")
    }

    private val screenItems = listOf(
        Destiny.Feed,
        Destiny.Search,
        Destiny.Post,
        Destiny.Notifications,
        Destiny.Profile
    )
}

internal class FeedViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(FeedScreenState())
    val uiState: StateFlow<FeedScreenState> = _uiState.asStateFlow()

    init {
        val postFirestore = PostFirestore()
        postFirestore.getAllPosts(
            onSuccess = { posts ->
                _uiState.value = _uiState.value.copy(posts = posts)
            },
            onError = {
                _uiState.value = _uiState.value.copy(posts = SampleData().posts)
            }
        )
    }
}

data class FeedScreenState(
    var posts: List<Post> = emptyList(),
)

internal class SessionViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(SessionState())
    val uiState: StateFlow<SessionState> = _uiState.asStateFlow()

    fun serCurrent(currentUser: UserAccount) {
        _uiState.value = _uiState.value.copy(userAccount = currentUser)
    }
}

data class SessionState(
    var appState: AppState = AppState.Loading,
    var userAccount: UserAccount = UserAccount(),
)


private enum class ComponentState { Pressed, Released }

@Composable
private fun GestureAnimationExample() {
    var useRed by remember { mutableStateOf(false) }
    var toState by remember { mutableStateOf(ComponentState.Released) }
    var coords by remember { mutableStateOf("") }
    val modifier = Modifier.pointerInput(Unit) {
        detectTapGestures(
            onPress = {
                coords = "x= ${it.x} y= ${it.y}"
                toState = ComponentState.Pressed
                tryAwaitRelease()
                toState = ComponentState.Released
            })
    }

    val transition: Transition<ComponentState> = updateTransition(targetState = toState, label = "")
    val scale: Float by transition.animateFloat(
        transitionSpec = { spring(stiffness = 50f) }, label = ""
    ) { state ->
        if (state == ComponentState.Pressed) 3f else 1f
    }

    val color: Color by transition.animateColor(
        transitionSpec = {
            if (this.initialState == ComponentState.Pressed
                && this.targetState == ComponentState.Released
            ) {
                spring(stiffness = 50f)
            } else {
                tween(durationMillis = 500)
            }
        }, label = ""
    ) { state ->
        when (state) {
            ComponentState.Pressed -> MaterialTheme.colorScheme.primary
            ComponentState.Released -> if (useRed) Color.Red else MaterialTheme.colorScheme.secondary
        }
    }
    Column {
        Button(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally),
            onClick = { useRed = !useRed }
        ) {
            Text(coords)
        }
        Box(
            modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
                .requiredSize((100 * scale).dp)
                .background(color)
        )
    }
}


@Composable
private fun GestureAnimationExampleOk() {
    val offsetY = remember { mutableStateOf(0f) }


    var useRed by remember { mutableStateOf(false) }
    var toState by remember { mutableStateOf(ComponentState.Released) }
    val modifier = Modifier.pointerInput(Unit) {
        detectTapGestures(onPress = {
            Log.i("GestureAnimati", "onPress y = ${it.y} x = ${it.x}")
            toState = ComponentState.Pressed
            tryAwaitRelease()
            toState = ComponentState.Released
        })
    }

    val transition: Transition<ComponentState> = updateTransition(targetState = toState, label = "")
    val scale: Float by transition.animateFloat(
        transitionSpec = { spring(stiffness = 50f) }, label = ""
    ) { state ->
        if (state == ComponentState.Pressed) 3f else 1f
    }


    val color: Color by transition.animateColor(
        transitionSpec = {
            if (this.initialState == ComponentState.Pressed
                && this.targetState == ComponentState.Released
            ) {
                spring(stiffness = 50f)
            } else {
                tween(durationMillis = 500)
            }
        }, label = ""
    ) { state ->
        when (state) {
            ComponentState.Pressed -> MaterialTheme.colorScheme.primary
            ComponentState.Released -> if (useRed) Color.Red else MaterialTheme.colorScheme.secondary
        }
    }
    Column(
        modifier = Modifier
            .fillMaxSize()
            .offset(0.dp, offsetY.value.dp)
            .pointerInput(Unit) {
                detectVerticalDragGestures { _, dragAmount ->
                    val originalY = offsetY.value
                    val newValue = (originalY + dragAmount).coerceIn(0f, 1000f)
                    offsetY.value = newValue
                }
            }

    ) {

        Text(
            text = "Scale: ${scale}", fontSize = 20.sp,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally)
        )

        Button(
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally)
//                .pointerInput(Unit) {
//                    detectVerticalDragGestures { _, dragAmount ->
//                        val originalY = offsetY.value
//                        val newValue = (originalY + dragAmount).coerceIn(0f, 1000f)
//                        offsetY.value = newValue
//                    }
//                }
            ,
            onClick = { useRed = !useRed }
        ) {
            Text("Coordenada: ${offsetY.value}")
        }
        Box(
            modifier
                .fillMaxSize()
                .wrapContentSize(Alignment.Center)
//                .requiredSize((100 * scale).dp)
                .background(color)
                .offset((100 * scale).dp)
        )
    }
}

@Composable
private fun DraggableText() {
    var offsetY by remember { mutableFloatStateOf(0f) }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            //.offset { IntOffset(offsetY.roundToInt(), 0) }
            .draggable(
                orientation = Orientation.Vertical,
                state = rememberDraggableState { delta ->
                    offsetY += delta
                }
            ),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Text(
            text = "Coordenada: $offsetY",
            style = MaterialTheme.typography.titleLarge.copy(
                fontSize = 20.sp
            ),
        )
    }
}

@Composable
fun TestDragAndDrop() {
    val offsetX = remember { mutableStateOf(80f) }
    val offsetY = remember { mutableStateOf(0f) }
    var height by remember { mutableStateOf(0f) }

    var toState by remember { mutableStateOf(ComponentState.Released) }

    LaunchedEffect(offsetY) {
        if (toState == ComponentState.Pressed) {
            Log.i("TestDragAndDrop", "Pressed")
            offsetY.value = offsetY.value
        } else {
            Log.i("TestDragAndDrop", "Released")
            offsetY.value = 0f
        }
    }

    Column(
        Modifier
            .fillMaxSize()
            .background(Color.LightGray)
            .pointerInput(Unit) {
                detectTapGestures(
                    onPress = {
                        toState = ComponentState.Pressed
                        tryAwaitRelease()
                        toState = ComponentState.Released
                    })
            },
    ) {

        AsyncImage(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
                .clipToBounds()
                .onSizeChanged { height = it.height.toFloat() },
            model = R.drawable.profile_pic_emoji_1,
            contentDescription = null,
        )

        Box(
            Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.Blue)
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        val originalY = offsetY.value
                        val newValue =
                            (originalY + dragAmount).coerceIn(0f, height - 50.dp.toPx())
                        offsetY.value = newValue
                    }
                }
        )

        Text(
            text = "Coordenada: ${offsetY.value}", fontSize = 20.sp,
            modifier = Modifier
                .padding(10.dp)
                .align(Alignment.CenterHorizontally)
        )
    }
}

@Composable
fun TestAnim1() {
    val offsetX = remember { mutableStateOf(0f) }
    val offsetY = remember { mutableStateOf(0f) }
    var height by remember { mutableStateOf(0f) }
    Box(
        Modifier
            .fillMaxSize()
            .onSizeChanged { height = it.height.toFloat() }
    ) {
        Box(
            Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.Blue)
                .pointerInput(Unit) {
                    awaitEachGesture {
                        val down = awaitFirstDown()
                        var change =
                            awaitVerticalTouchSlopOrCancellation(down.id) { change, over ->
                                val originalY = offsetY.value
                                val newValue = (originalY + over)
                                    .coerceIn(0f, height - 50.dp.toPx())
                                change.consume()
                                offsetY.value = newValue
                            }
                        while (change != null && change.pressed) {
                            change = awaitVerticalDragOrCancellation(change.id)
                            if (change != null && change.pressed) {
                                val originalY = offsetY.value
                                val newValue = (originalY + change.positionChange().y)
                                    .coerceIn(0f, height - 50.dp.toPx())
                                change.consume()
                                offsetY.value = newValue
                            }
                        }
                    }
                }
        )
    }
}

@Composable
fun TestAnim2() {
    val offsetX = remember { mutableStateOf(0f) }
    val offsetY = remember { mutableStateOf(0f) }
    var height by remember { mutableStateOf(0f) }
    Box(
        Modifier
            .fillMaxSize()
            .onSizeChanged { height = it.height.toFloat() }
    ) {
        Box(
            Modifier
                .offset { IntOffset(offsetX.value.roundToInt(), offsetY.value.roundToInt()) }
                .fillMaxWidth()
                .height(50.dp)
                .background(Color.Blue)
                .pointerInput(Unit) {
                    detectVerticalDragGestures { _, dragAmount ->
                        val originalY = offsetY.value
                        val newValue = (originalY + dragAmount).coerceIn(0f, height - 50.dp.toPx())
                        offsetY.value = newValue
                    }
                }
        )
    }
}


@Composable
fun TestAnim3() {
    Box(
        Modifier
            .size(200.dp)
            .clipToBounds()
            .background(Color.LightGray)
    ) {
        // set up all transformation states
        var scale by remember { mutableStateOf(1f) }
        var rotation by remember { mutableStateOf(0f) }
        var offset by remember { mutableStateOf(Offset.Zero) }
        val coroutineScope = rememberCoroutineScope()
        // let's create a modifier state to specify how to update our UI state defined above
        val state = rememberTransformableState { zoomChange, offsetChange, rotationChange ->
            // note: scale goes by factor, not an absolute difference, so we need to multiply it
            // for this example, we don't allow downscaling, so cap it to 1f
            scale = max(scale * zoomChange, 1f)
            rotation += rotationChange
            offset += offsetChange
        }
        Box(
            Modifier
                // apply pan offset state as a layout transformation before other modifiers
                .offset { IntOffset(offset.x.roundToInt(), offset.y.roundToInt()) }
                // add transformable to listen to multitouch transformation events after offset
                .transformable(state = state)
                // optional for example: add double click to zoom
                .pointerInput(Unit) {
                    detectTapGestures(
                        onDoubleTap = {
                            coroutineScope.launch { state.animateZoomBy(4f) }
                        }
                    )
                }
                .fillMaxSize()
                .border(1.dp, Color.Green),
            contentAlignment = Alignment.Center
        ) {
            Text(
                "\uD83C\uDF55",
                fontSize = 32.sp,
                // apply other transformations like rotation and zoom on the pizza slice emoji
                modifier = Modifier.graphicsLayer {
                    scaleX = scale
                    scaleY = scale
                    rotationZ = rotation
                }
            )
        }
    }
}


// 29/07/2023
enum class DragAnchors(val fraction: Float) {
    Start(.050f),
    End(.5f),
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalDraggableSample(
    modifier: Modifier = Modifier,
) {
    var imageSize by remember {
        mutableStateOf(0.dp)
    }

    var currentPosition by remember {
        mutableStateOf(0.dp)
    }

    val minHeightImage = 32.dp

    val density = LocalDensity.current
    val positionalThreshold = { distance: Float -> distance * 0.5f }
    val velocityThreshold = { with(density) { 100.dp.toPx() } }
    val animationSpec = tween<Float>()
    val state = rememberSaveable(
        density,
        saver = AnchoredDraggableState.Saver(
            animationSpec = animationSpec,
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold,
        )
    ) {
        AnchoredDraggableState(
            initialValue = DragAnchors.Start,
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold,
            animationSpec = animationSpec,
        )
    }
    val contentSize = 80.dp
    val contentSizePx = with(density) { contentSize.toPx() }
    Box(
        modifier
            .onSizeChanged { layoutSize ->
                val dragEndPoint = layoutSize.height - contentSizePx
                state.updateAnchors(
                    DraggableAnchors {
                        DragAnchors
                            .values()
                            .forEach { anchor ->
                                anchor at dragEndPoint * anchor.fraction
                            }
                    }
                )
            }
    ) {
        DraggableContent(
            modifier = Modifier
                .size(contentSize)
                .offset {
                    val intOffset = IntOffset(
                        x = 0,
                        y = state
                            .requireOffset()
                            .roundToInt(),
                    )
                    currentPosition = if (intOffset.y.dp != 0.dp) (intOffset.y.dp / 8) else 0.dp
                    imageSize =
                        if (currentPosition < minHeightImage) minHeightImage else currentPosition
                    intOffset
                }
                .anchoredDraggable(state, Orientation.Vertical),
        )
    }

    Column(
        Modifier.fillMaxHeight(),
    ) {
        Image(
            painter = painterResource(id = R.drawable.ic_logo_colors),
            contentDescription = null,
            modifier = Modifier
                .size(imageSize)
                .align(Alignment.CenterHorizontally)
        )

        Column(
            Modifier.fillMaxHeight(),
            verticalArrangement = Arrangement.Center
        ) {
            Text(
                text = "Teste: newSize = $imageSize",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()

            )
            Text(
                text = "position = $currentPosition - contentSizePx = ${state.currentValue}",
                fontSize = 24.sp,
                textAlign = TextAlign.Center,
                modifier = Modifier
                    .padding(10.dp)
                    .fillMaxWidth()

            )
        }

        // Quando o arrastar chegar no final, volta pa o inicio com uma animação
        if (state.currentValue == DragAnchors.End) {
            LaunchedEffect(Unit) {
                state.animateTo(DragAnchors.Start)
            }
        }
    }
}

@Composable
fun DraggableContent(
    modifier: Modifier = Modifier,
) {
    Image(
        painter = painterResource(id = R.drawable.profile_pic_emoji_3),
        modifier = modifier,
        contentDescription = null,
    )
}


enum class DragAnchorsOkButCanBetter(val fraction: Float) {
    Start(0f),
    OneQuarter(.25f),
    Half(.5f),
    ThreeQuarters(.75f),
    End(1f),
}


@OptIn(ExperimentalFoundationApi::class)
@Composable
fun VerticalDraggableSampleOkButCanBetter(
    modifier: Modifier = Modifier,
) {
    val density = LocalDensity.current
    val positionalThreshold = { distance: Float -> distance * 0.5f }
    val velocityThreshold = { with(density) { 100.dp.toPx() } }
    val animationSpec = tween<Float>()
    val state = rememberSaveable(
        density,
        saver = AnchoredDraggableState.Saver(
            animationSpec = animationSpec,
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold,
        )
    ) {
        AnchoredDraggableState(
            initialValue = DragAnchorsOkButCanBetter.Half,
            positionalThreshold = positionalThreshold,
            velocityThreshold = velocityThreshold,
            animationSpec = animationSpec,
        )
    }
    val contentSize = 80.dp
    val contentSizePx = with(density) { contentSize.toPx() }
    Box(
        modifier
            .onSizeChanged { layoutSize ->
                val dragEndPoint = layoutSize.height - contentSizePx
                state.updateAnchors(
                    DraggableAnchors {
                        DragAnchorsOkButCanBetter
                            .values()
                            .forEach { anchor ->
                                anchor at dragEndPoint * anchor.fraction
                            }
                    }
                )
            }
    ) {
        DraggableContent(
            modifier = Modifier
                .size(contentSize)
                .offset {
                    IntOffset(
                        x = 0,
                        y = state
                            .requireOffset()
                            .roundToInt(),
                    )
                }
                .anchoredDraggable(state, Orientation.Vertical),
        )
    }

    Text(
        text = "Teste: ${positionalThreshold.invoke(100f)}",
        fontSize = 24.sp,
        textAlign = TextAlign.Center,
        modifier = Modifier
            .fillMaxWidth()

    )
}