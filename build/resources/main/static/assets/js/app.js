$('#add-ingredient').click(function () {
    let ingredientCount = $('.ingredient-row').length;
    let ingredientRow = '<div class="ingredient-row">' +
        '<div >' +
        '<hr class="addRows">' +
        '<p> <input placeholder="Ingredient Name" type="text" id="ingredients' + ingredientCount + '.item" name="ingredients[' + ingredientCount + '].name" />' +
        '</p> </div>' +
        '<div>' +
        '<p> <input placeholder="Ingredient Condition" type="text" id="ingredients' + ingredientCount + '.ingredientCondition" name="ingredients[' + ingredientCount + '].ingredientCondition" />' +
        '</p> </div>' +
        '<div">' +
        '<p> <input placeholder="Ingredient Quantity" type="text" id="ingredients' + ingredientCount + '.quantity" name="ingredients[' + ingredientCount + '].quantity" />' +
        '</p> </div>' +
        '</div>';
    $("#add-ingredient-row").before(ingredientRow);
});

$('#add-step').click(function () {
    let stepCount = $('.step-row').length;
    let stepRow = '<div class="step-row">' +
        '<div>' +
        '<hr class="addRows">' +
        '<p>' +
        '<input placeholder="Step Description" type="text" id="steps' + stepCount + '.stepName" name="steps[' + stepCount + '].name" />' +
        '</p>' +
        '</div>' +
        '</div>'
    $("#add-step-row").before(stepRow);
});

$(document).on("change", "#select-category", function () {
    let category = $('#select-category').val();
    window.location.href = '/recipes/category/' + category;
});

$('#search').on('keydown', function (e) {
    if (e.which === 13 && this.value.length < 13) {
        $(this).prop("disabled", true);
        let search = $('#search').val();
        window.location.href = '/recipes/search?' + 'searchq' + '=' + search;
    }
});

let id = "";

$('.favorite-button-index').each(function () {
    $(this).click(function () {
        id = $(this).attr('id');
    });
});


$(".favorite").on("submit", function (e) {

    e.preventDefault();

    $.post(this.action, function (data) {
        let imageUrl = (data.favorited) ? '/assets/images/favorited.svg' : '/assets/images/favorite.svg'
        $('#' + id + '>img').attr('src', imageUrl);
    });
})

$(".favorite-detail").on("submit", function (e) {

    e.preventDefault();

    $.post(this.action, function (data) {
        let imageUrl = (data.favorited) ? '/assets/images/favorited.svg' : '/assets/images/favorite.svg'
        $('#favorite-button-detail>img').attr('src', imageUrl);
    });
})


$('.upvote').click(function(e) {
    const direction = 1;
    const recipeId = $(this).attr("data-id")
    const voteSum = $("#votecount-" + recipeId).html()

    $.get(`http://localhost:8080/vote/recipe/${recipeId}/direction/${direction}/votecount/${voteSum}`, function (data) {
        $("#votecount-" + recipeId).html(data)
    })
})

$('.downvote').click(function(e) {
    const direction = -1;
    const recipeId = $(this).attr("data-id")
    const voteSum = $("#votecount-" + recipeId).html()

    $.get(`http://localhost:8080/vote/recipe/${recipeId}/direction/${direction}/votecount/${voteSum}`, function (data) {
        $("#votecount-" + recipeId).html(data)
    })
})

let max = 50;
let tot, str;
$('.test').each(function() {
    str = String($(this).html());
    tot = str.length;
    str = (tot <= max)
        ? str
        : str.substring(0,(max + 1))+"...";
    $(this).html(str);
});
