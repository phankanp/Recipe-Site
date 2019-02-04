$('#add-ingredient').click(function () {
    var ingredientCount = $('.ingredient-row').length;
    var ingredientRow = '<div class="ingredient-row">' +
        '<div class="prefix-20 grid-30">' +
        '<p> <input type="text" id="ingredients' + ingredientCount + '.item" name="ingredients[' + ingredientCount + '].name" />' +
        '</p> </div>' +
        '<div class="grid-30">' +
        '<p> <input type="text" id="ingredients' + ingredientCount + '.ingredientCondition" name="ingredients[' + ingredientCount + '].ingredientCondition" />' +
        '</p> </div>' +
        '<div class="grid-10 suffix-10">' +
        '<p> <input type="text" id="ingredients' + ingredientCount + '.quantity" name="ingredients[' + ingredientCount + '].quantity" />' +
        '</p> </div>' +
        '</div>'
    $("#add-ingredient-row").before(ingredientRow);
});

$('#add-step').click(function () {
    var stepCount = $('.step-row').length;
    var stepRow = '<div class="step-row">' +
        '<div class="prefix-20 grid-80">' +
        '<p>' +
        '<input  type="text" id="steps' + stepCount + '.stepName" name="steps[' + stepCount + '].name" />' +
        '</p>' +
        '</div>' +
        '</div>'
    $("#add-step-row").before(stepRow);
});

$(document).on("change", "#select-category", function () {
    var category = $('#select-category').val();
    window.location.href = '/recipes/category/' + category;
});

$('#search').on('keydown', function (e) {
    if (e.which === 13 && this.value.length < 13) {
        $(this).prop("disabled", true);
        var search = $('#search').val();
        window.location.href = '/recipes/search?' + 'searchq' + '=' + search;
    }
});

var id = "";

$('.favorite-button-index').each(function () {
    $(this).click(function () {
        id = $(this).attr('id');
    });
});


$(".favorite").on("submit", function (e) {

    e.preventDefault();

    $.post(this.action, function (data) {
        var imageUrl = (data.favorited) ? '/assets/images/favorited.svg' : '/assets/images/favorite.svg'
        $('#' + id + '>img').attr('src', imageUrl);
    });
})

$(".favorite-detail").on("submit", function (e) {

    e.preventDefault();

    $.post(this.action, function (data) {
        var imageUrl = (data.favorited) ? '/assets/images/favorited.svg' : '/assets/images/favorite.svg'
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
