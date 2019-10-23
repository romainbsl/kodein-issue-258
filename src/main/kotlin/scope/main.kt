import org.kodein.di.Kodein
import org.kodein.di.bindings.Scope
import org.kodein.di.bindings.ScopeRegistry
import org.kodein.di.bindings.StandardScopeRegistry
import org.kodein.di.generic.*

fun main() {
    val kodein = Kodein {
        import(module)
    }

    val context1 = GlobalKodeinContext("context 1")
    val context2 = GlobalKodeinContext("context 2")

    B(kodein, context1).showInstance()
    C(kodein, context1).showInstance()
    B(kodein, context2).showInstance()
    C(kodein, context2).showInstance()
}

object SingletonScope : Scope<GlobalKodeinContext> {
    override fun getRegistry(context: GlobalKodeinContext): ScopeRegistry =
            context.standardScopeRegistry as? ScopeRegistry
                    ?: StandardScopeRegistry().also { context.standardScopeRegistry = it }

}

class GlobalKodeinContext(val value: String) {
    var standardScopeRegistry = StandardScopeRegistry()
}

class InjectableClass {
    var value: String? = null

    fun initValue() {
        value = "World"
    }
    fun shwoValue(): String {
        return value ?: throw NullPointerException()
    }
}

val module = Kodein.Module("deployment_config_manager", false) {
    bind<InjectableClass>() with scoped(SingletonScope).singleton { InjectableClass() }
}

class B(val kodein: Kodein, val context: GlobalKodeinContext){
    private val injectable: InjectableClass by kodein.on(context).instance()

    fun showInstance() {
        println(injectable)
        injectable.initValue()
    }
}
class C(val kodein: Kodein, val context: GlobalKodeinContext) {
    private val injectable: InjectableClass by kodein.on(context).instance()

    fun showInstance() {
        println(injectable)
        println("Hello, " + injectable.shwoValue() + " from ${context.value}")
    }
}