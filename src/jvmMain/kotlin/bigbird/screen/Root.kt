package bigbird.screen

import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import app.cash.nostrino.crypto.SecKey
import bigbird.screen.RootComponent.Child.HomeChild
import bigbird.screen.RootComponent.Child.LoginChild
import com.arkivanov.decompose.ComponentContext
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.Children
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.fade
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.plus
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.scale
import com.arkivanov.decompose.extensions.compose.jetbrains.stack.animation.stackAnimation
import com.arkivanov.decompose.router.stack.ChildStack
import com.arkivanov.decompose.router.stack.StackNavigation
import com.arkivanov.decompose.router.stack.childStack
import com.arkivanov.decompose.router.stack.push
import com.arkivanov.decompose.value.Value
import com.arkivanov.essenty.parcelable.Parcelable


// https://arkivanov.github.io/Decompose/getting-started/quick-start/#using-value-from-decompose to grok this
interface RootComponent {
    val stack: Value<ChildStack<*, Child>>

    sealed class Child {
        class LoginChild(val component: LoginComponent) : Child()
        class HomeChild(val component: HomeComponent) : Child()
    }
}

class DefaultRootComponent(
    componentContext: ComponentContext,
) : RootComponent, ComponentContext by componentContext {

    private val navigation = StackNavigation<Config>()

    private val _stack = childStack(
        source = navigation,
        initialConfiguration = Config.Login, // The initial child component is Home
        handleBackButton = true, // Automatically pop from the stack on back button presses
        childFactory = ::child,
    )

    override val stack: Value<ChildStack<*, RootComponent.Child>> = _stack

    private fun child(config: Config, componentContext: ComponentContext): RootComponent.Child =
        when (config) {
            is Config.Login -> LoginChild(loginComponent(componentContext))
            is Config.Home -> HomeChild(homeComponent(componentContext, config))
        }

    private fun loginComponent(componentContext: ComponentContext): LoginComponent = DefaultLoginComponent(
        componentContext,
        onLogin = { secKey ->
            navigation.push(Config.Home(secKey))
        },
    )

    private fun homeComponent(componentContext: ComponentContext, config: Config.Home): HomeComponent = DefaultHomeComponent(
        componentContext,
        config.secKey,
    )

    private sealed interface Config : Parcelable {
        object Login : Config
        data class Home(val secKey: SecKey) : Config
    }
}

@Composable
fun RootContent(component: RootComponent, modifier: Modifier = Modifier) {
    Children(
        stack = component.stack,
        modifier = modifier,
        animation = stackAnimation(fade() + scale()),
    ) {
        when (val child = it.instance) {
            is LoginChild -> LoginContent(component = child.component)
            is HomeChild -> HomeContent(component = child.component)
        }
    }
}
