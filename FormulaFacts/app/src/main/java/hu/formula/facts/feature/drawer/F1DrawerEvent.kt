package hu.formula.facts.feature.drawer

sealed class F1DrawerEvent {
    data object OpenDrawer : F1DrawerEvent()
}