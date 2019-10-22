import org.kodein.di.Kodein
import org.kodein.di.bindings.Scope
import org.kodein.di.bindings.ScopeRegistry
import org.kodein.di.bindings.StandardScopeRegistry
import org.kodein.di.generic.*

fun main() {
    val kodein = Kodein {
        import(module)
    }
    B(kodein).showInstance()
    C(kodein).showInstance()
}

object SingletonScope : Scope<GlobalKodeinContext> {
    override fun getRegistry(context: GlobalKodeinContext): ScopeRegistry =
            context.standardScopeRegistry as? ScopeRegistry
                    ?: StandardScopeRegistry().also { context.standardScopeRegistry = it }

}

object GlobalKodeinContext {
    var standardScopeRegistry = StandardScopeRegistry()
}

class InjectableClass

val module = Kodein.Module("deployment_config_manager", false) {
    bind<InjectableClass>() with scoped(SingletonScope).singleton { InjectableClass() }
}

class B(val kodein: Kodein){
    private val configManager: InjectableClass by kodein.on(context = GlobalKodeinContext).instance()

    fun showInstance() {
        println(configManager)
    }
}
class C(val kodein: Kodein) {
    private val configManager: InjectableClass by kodein.on(context = GlobalKodeinContext).instance()

    fun showInstance() {
        println(configManager)
    }
}