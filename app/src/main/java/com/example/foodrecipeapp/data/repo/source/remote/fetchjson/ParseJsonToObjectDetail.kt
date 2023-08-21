package com.example.foodrecipeapp.data.repo.source.remote.fetchjson

import com.example.foodrecipeapp.data.model.AnalyzedInstruction
import com.example.foodrecipeapp.data.model.AnalyzedInstructionEntry
import com.example.foodrecipeapp.data.model.Equipment
import com.example.foodrecipeapp.data.model.EquipmentEntry
import com.example.foodrecipeapp.data.model.ExtendedIngredient
import com.example.foodrecipeapp.data.model.ExtendedIngredientEntry
import com.example.foodrecipeapp.data.model.Ingredient
import com.example.foodrecipeapp.data.model.IngredientEntry
import com.example.foodrecipeapp.data.model.Length
import com.example.foodrecipeapp.data.model.LengthEntry
import com.example.foodrecipeapp.data.model.Measure
import com.example.foodrecipeapp.data.model.MeasureEntry
import com.example.foodrecipeapp.data.model.Metric
import com.example.foodrecipeapp.data.model.MetricEntry
import com.example.foodrecipeapp.data.model.ProductMatch
import com.example.foodrecipeapp.data.model.ProductMatchEntry
import com.example.foodrecipeapp.data.model.RecipeDetail
import com.example.foodrecipeapp.data.model.RecipeDetailEntry
import com.example.foodrecipeapp.data.model.Step
import com.example.foodrecipeapp.data.model.StepEntry
import com.example.foodrecipeapp.data.model.Us
import com.example.foodrecipeapp.data.model.UsEntry
import com.example.foodrecipeapp.data.model.WinePairing
import com.example.foodrecipeapp.data.model.WinePairingEntry
import org.json.JSONArray
import org.json.JSONObject

@Suppress("TooManyFunctions")
class ParseJsonToObjectDetail {
    fun parseJsonToRecipeObject(jsonObject: JSONObject?): RecipeDetail {
        val recipeDetail = RecipeDetail()

        jsonObject?.let {
            recipeDetail.apply {
                vegetarian = it.optBoolean(RecipeDetailEntry.VEGETARIAN_KEY)
                vegan = it.optBoolean(RecipeDetailEntry.VEGAN_KEY)
                glutenFree = it.optBoolean(RecipeDetailEntry.GLUTEN_FREE_KEY)
                dairyFree = it.optBoolean(RecipeDetailEntry.DAIRY_FREE_KEY)
                veryHealthy = it.optBoolean(RecipeDetailEntry.VERY_HEALTHY_KEY)
                cheap = it.optBoolean(RecipeDetailEntry.CHEAP_KEY)
                veryPopular = it.optBoolean(RecipeDetailEntry.VERY_POPULAR_KEY)
                sustainable = it.optBoolean(RecipeDetailEntry.SUSTAINABLE_KEY)
                weightWatcherSmartPoints =
                    it.optInt(RecipeDetailEntry.WEIGHT_WATCHER_SMART_POINTS_KEY)
                gaps = it.optString(RecipeDetailEntry.GAPS_KEY)
                lowFodmap = it.optBoolean(RecipeDetailEntry.LOW_FOD_MAP_KEY)
                preparationMinutes = it.optInt(RecipeDetailEntry.PREPARATION_MINUTES_KEY)
                cookingMinutes = it.optInt(RecipeDetailEntry.COOKING_MINUTES_KEY)
                aggregateLikes = it.optInt(RecipeDetailEntry.AGGREGATE_LIKES_KEY)
                healthScore = it.optDouble(RecipeDetailEntry.HEALTH_SCORE_KEY)
                creditsText = it.optString(RecipeDetailEntry.CREDITS_TEXT_KEY)
                sourceName = it.optString(RecipeDetailEntry.SOURCE_NAME_KEY)
                pricePerServing = it.optDouble(RecipeDetailEntry.PRICE_PER_SERVING_KEY)

                // Handle extended ingredients array
                extendedIngredients =
                    parseExtendedIngredients(it.getJSONArray(RecipeDetailEntry.EXTENDED_INGREDIENTS_KEY))

                id = it.optInt(RecipeDetailEntry.ID_KEY)
                title = it.optString(RecipeDetailEntry.TITLE_KEY)
                readyInMinutes = it.optInt(RecipeDetailEntry.READY_IN_MINUTES_KEY)
                servings = it.optInt(RecipeDetailEntry.SERVINGS_KEY)
                sourceUrl = it.optString(RecipeDetailEntry.SOURCE_URL_KEY)
                image = it.optString(RecipeDetailEntry.IMAGE_KEY)
                imageType = it.optString(RecipeDetailEntry.IMAGE_TYPE_KEY)
                summary = it.optString(RecipeDetailEntry.SUMMARY_KEY)

                // Handle cuisines array
                cuisines = parseStringArray(it.getJSONArray(RecipeDetailEntry.CUISINES_KEY))

                // Handle dish types array
                dishTypes = parseStringArray(it.getJSONArray(RecipeDetailEntry.DISH_TYPES_KEY))

                // Handle diets array
                diets = parseStringArray(it.getJSONArray(RecipeDetailEntry.DIETS_KEY))

                // Handle occasions array
                occasions = parseStringArray(it.getJSONArray(RecipeDetailEntry.OCCASIONS_KEY))

                // Handle wine pairing object
                winePairing = parseWinePairing(it.getJSONObject(RecipeDetailEntry.WINE_PAIRING_KEY))

                instructions = it.optString(RecipeDetailEntry.INSTRUCTIONS_KEY)

                analyzedInstructions =
                    parseAnalyzedInstructions(it.getJSONArray(RecipeDetailEntry.ANALYZED_INSTRUCTIONS_KEY))
                spoonacularSourceUrl = it.optString(RecipeDetailEntry.SPOONACULAR_SOURCE_URL_KEY)
            }
        }

        return recipeDetail
    }

    private fun parseExtendedIngredients(jsonArray: JSONArray): MutableList<ExtendedIngredient> {
        val extendedIngredients = mutableListOf<ExtendedIngredient>()

        // Handle extendedIngredients array
        for (i in 0 until jsonArray.length()) {
            val extendedIngredientJsonObject = jsonArray.getJSONObject(i)
            val measure =
                parseMeasure(extendedIngredientJsonObject.getJSONObject(ExtendedIngredientEntry.MEASURES_KEY))
            val meta =
                parseStringArray(extendedIngredientJsonObject.getJSONArray(ExtendedIngredientEntry.META_KEY))

            val extendedIngredient = ExtendedIngredient(
                id = extendedIngredientJsonObject.optInt(ExtendedIngredientEntry.ID_KEY),
                aisle = extendedIngredientJsonObject.optString(ExtendedIngredientEntry.AISLE_KEY),
                image = extendedIngredientJsonObject.optString(ExtendedIngredientEntry.IMAGE_KEY),
                consistency = extendedIngredientJsonObject.optString(ExtendedIngredientEntry.CONSISTENCY_KEY),
                name = extendedIngredientJsonObject.optString(ExtendedIngredientEntry.NAME_KEY),
                nameClean = extendedIngredientJsonObject.optString(ExtendedIngredientEntry.NAME_CLEAN_KEY),
                original = extendedIngredientJsonObject.optString(ExtendedIngredientEntry.ORIGINAL_KEY),
                originalName = extendedIngredientJsonObject.optString(ExtendedIngredientEntry.ORIGINAL_NAME_KEY),
                amount = extendedIngredientJsonObject.getDouble(ExtendedIngredientEntry.AMOUNT_KEY),
                unit = extendedIngredientJsonObject.optString(ExtendedIngredientEntry.UNIT_KEY),
                meta = meta,
                measure = measure
            )

            extendedIngredients.add(extendedIngredient)
        }

        return extendedIngredients
    }

    private fun parseMeasure(measureObject: JSONObject): Measure {
        val usObject = measureObject.getJSONObject(MeasureEntry.US_KEY)
        val metricObject = measureObject.getJSONObject(MeasureEntry.METRIC_KEY)

        return Measure(
            us = Us(
                amount = usObject.optDouble(UsEntry.AMOUNT_KEY),
                unitShort = usObject.optString(UsEntry.UNIT_SHORT_KEY),
                unitLong = usObject.optString(UsEntry.UNIT_LONG_KEY)
            ),
            metric = Metric(
                amount = metricObject.optDouble(MetricEntry.AMOUNT_KEY),
                unitShort = metricObject.optString(MetricEntry.UNIT_SHORT_KEY),
                unitLong = metricObject.optString(MetricEntry.UNIT_LONG_KEY)
            )
        )
    }

    private fun parseStringArray(jsonArray: JSONArray): MutableList<String> {
        val list = mutableListOf<String>()

        for (i in 0 until jsonArray.length()) {
            list.add(jsonArray.getString(i))
        }

        return list
    }

    private fun parseWinePairing(winePairingObject: JSONObject): WinePairing {
        val winePairing = WinePairing()

        winePairingObject.optJSONObject(RecipeDetailEntry.WINE_PAIRING_KEY)
            ?.let { winePairingJsonObject ->
                val pairedWinesJsonArray =
                    winePairingJsonObject.optJSONArray(WinePairingEntry.PAIRED_WINES_KEY)
                val productMatchesJsonArray =
                    winePairingJsonObject.optJSONArray(WinePairingEntry.PRODUCT_MATCH_KEY)

                val pairedWines = pairedWinesJsonArray?.let { parseStringArray(it) }
                val productMatches = parseProductMatches(productMatchesJsonArray)

                winePairing.apply {
                    if (pairedWines != null) {
                        this.pairedWines = pairedWines
                    }
                    pairingText = winePairingJsonObject.optString(WinePairingEntry.PAIRING_TEXT_KEY)
                    this.productMatches = productMatches
                }
            }

        return winePairing
    }

    private fun parseProductMatches(jsonArray: JSONArray?): MutableList<ProductMatch> {
        val productMatches = mutableListOf<ProductMatch>()

        jsonArray?.let {
            for (i in 0 until it.length()) {
                val productMatchJsonObject = it.getJSONObject(i)
                val productMatch = ProductMatch(
                    id = productMatchJsonObject.optInt(ProductMatchEntry.ID_KEY),
                    title = productMatchJsonObject.optString(ProductMatchEntry.TITLE_KEY),
                    description = productMatchJsonObject.optString(ProductMatchEntry.DESCRIPTION_KEY),
                    price = productMatchJsonObject.optString(ProductMatchEntry.PRICE_KEY),
                    imageUrl = productMatchJsonObject.optString(ProductMatchEntry.IMAGE_URL_KEY),
                    averageRating = productMatchJsonObject.optDouble(ProductMatchEntry.AVERAGE_RATING_KEY),
                    ratingCount = productMatchJsonObject.optDouble(ProductMatchEntry.RATING_COUNT_KEY),
                    score = productMatchJsonObject.optDouble(ProductMatchEntry.SCORE_KEY),
                    link = productMatchJsonObject.optString(ProductMatchEntry.LINK_KEY)
                )
                productMatches.add(productMatch)
            }
        }

        return productMatches
    }

    private fun parseAnalyzedInstructions(jsonArray: JSONArray): MutableList<AnalyzedInstruction> {
        val analyzedInstructions = mutableListOf<AnalyzedInstruction>()

        for (i in 0 until jsonArray.length()) {
            val analyzedInstructionJsonObject = jsonArray.getJSONObject(i)
            val name = analyzedInstructionJsonObject.optString(AnalyzedInstructionEntry.NAME_KEY)
            val stepsJsonArray =
                analyzedInstructionJsonObject.getJSONArray(AnalyzedInstructionEntry.STEP_KEY)
            val steps = parseSteps(stepsJsonArray)

            val analyzedInstruction = AnalyzedInstruction(
                name = name,
                steps = steps
            )

            analyzedInstructions.add(analyzedInstruction)
        }

        return analyzedInstructions
    }

    private fun parseSteps(jsonArray: JSONArray): MutableList<Step> {
        val steps = mutableListOf<Step>()

        for (i in 0 until jsonArray.length()) {
            val stepJsonObject = jsonArray.getJSONObject(i)
            val ingredientsJsonArray = stepJsonObject.getJSONArray(StepEntry.INGREDIENTS_KEY)
            val equipmentsJsonArray = stepJsonObject.getJSONArray(StepEntry.EQUIPMENTS_KEY)

            val ingredients = parseIngredients(ingredientsJsonArray)
            val equipments = parseEquipments(equipmentsJsonArray)

            val step =
                stepJsonObject.optJSONObject(StepEntry.LENGTH_KEY)?.let { parseLength(it) }?.let {
                    Step(
                        number = stepJsonObject.optInt(StepEntry.NUMBER_KEY),
                        step = stepJsonObject.optString(StepEntry.STEP_KEY),
                        ingredients = ingredients,
                        equipments = equipments,
                        length = it
                    )
                }

            if (step != null) {
                steps.add(step)
            }
        }

        return steps
    }

    private fun parseIngredients(jsonArray: JSONArray): MutableList<Ingredient> {
        val ingredients = mutableListOf<Ingredient>()

        for (i in 0 until jsonArray.length()) {
            val ingredientJsonObject = jsonArray.getJSONObject(i)
            val ingredient = Ingredient(
                id = ingredientJsonObject.optInt(IngredientEntry.ID_KEY),
                name = ingredientJsonObject.optString(IngredientEntry.NAME_KEY),
                localizedName = ingredientJsonObject.optString(IngredientEntry.LOCALIZED_NAME_KEY),
                image = ingredientJsonObject.optString(IngredientEntry.IMAGE_KEY)
            )
            ingredients.add(ingredient)
        }

        return ingredients
    }

    private fun parseEquipments(jsonArray: JSONArray): MutableList<Equipment> {
        val equipments = mutableListOf<Equipment>()

        for (i in 0 until jsonArray.length()) {
            val equipmentJsonObject = jsonArray.getJSONObject(i)
            val equipment = Equipment(
                id = equipmentJsonObject.optInt(EquipmentEntry.ID_KEY),
                name = equipmentJsonObject.optString(EquipmentEntry.NAME_KEY),
                localizedName = equipmentJsonObject.optString(EquipmentEntry.LOCALIZED_NAME_KEY),
                image = equipmentJsonObject.optString(EquipmentEntry.IMAGE_KEY)
            )
            equipments.add(equipment)
        }

        return equipments
    }

    private fun parseLength(lengthObject: JSONObject): Length {
        return Length(
            number = lengthObject.optInt(LengthEntry.NUMBER_KEY),
            unit = lengthObject.optString(LengthEntry.UNIT_KEY)
        )
    }
}
