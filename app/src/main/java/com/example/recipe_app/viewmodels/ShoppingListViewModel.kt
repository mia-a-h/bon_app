import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.recipe_app.model.Recipe
import com.example.recipe_app.model.ShoppingList
import com.example.recipe_app.services.ShoppingListSaveService
import kotlinx.coroutines.launch

class ShoppingListViewModel : ViewModel() {
    private val shoppingListSaveService = ShoppingListSaveService()

    private val _shoppingLists = MutableLiveData<List<ShoppingList>>()
    val shoppingLists: LiveData<List<ShoppingList>> = _shoppingLists

    fun saveShoppingList(recipe: Recipe) {
        // Call the saveShoppingListToFirestore function in ShoppingListSaveService
        viewModelScope.launch {
            shoppingListSaveService.saveShoppingListToFirestore(recipe)
        }
    }

    // Retrieve shopping list
    fun getShoppingList(userId: String) {
        viewModelScope.launch {
            shoppingListSaveService.getShoppingListFromFirestore(userId) { shoppingLists ->
                _shoppingLists.postValue(shoppingLists)
            }
        }
    }

    // Delete shopping list
    fun deleteShoppingList(userId: String, recipeId: String) {
        viewModelScope.launch {
            shoppingListSaveService.deleteShoppingListFromFirestore(userId, recipeId)
        }
    }
}
