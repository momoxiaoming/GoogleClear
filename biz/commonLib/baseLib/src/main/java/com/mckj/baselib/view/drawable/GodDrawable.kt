package com.mckj.baselib.view.drawable

import android.graphics.drawable.Drawable
import android.graphics.drawable.GradientDrawable
import android.graphics.drawable.StateListDrawable
import android.view.View
import androidx.annotation.ColorInt
import androidx.annotation.IntDef
import androidx.databinding.BindingAdapter
import com.org.openlib.ui.drawable.TempIndex
import org.jetbrains.anko.dip


/**
 * GodDrawable
 *
 * 通过注解 通用view drawable背景方案
 *
 * 目前支持 normal,checked,checkable,focused,pressed,selected,enabled  状态drawable
 *
 * @author mmxm
 * @date 2021/6/22 10:10
 */
@BindingAdapter(
    value = [
        "god_normal_radius",                    //全体圆角
        "god_normal_radiusLT",                  //圆角-左上
        "god_normal_radiusLB",                  //圆角-左下
        "god_normal_radiusRB",                  //圆角-右下
        "god_normal_radiusRT",                  //圆角-右上
        "god_normal_solidColor",                //填充色
        "god_normal_strokeColor",               //边框色
        "god_normal_strokeWidth",               //边框宽度
        "god_normal_strokeDash",                //边框虚线宽度间隔宽度
        "god_normal_strokeDashWidth",           //边框虚线长度
        "god_normal_gradient_angle",            // 渐变方向 参考注解[GOrientation],0 上到下,4下到上 ,2 右到左,6 左到右
        "god_normal_gradient_type",             // 渐变类型, [0,1,2] 0 线性渐变,1 径向渐变,2 圆形扫描渐变
        "god_normal_gradient_startColor",       //渐变色-起点
        "god_normal_gradient_centerColor",      //填充色-中间
        "god_normal_gradient_endColor",         //渐变色-终点
        "god_normal_gradient_centerX",          //渐变色-中心点x,当渐变类型为RADIAL时有效
        "god_normal_gradient_centerY",          //渐变色-中心点Y,当渐变类型为RADIAL时有效
        "god_normal_gradient_radius",           //渐变色-半径,当渐变类型为RADIAL时有效

        "god_checked_radius",                    //全体圆角
        "god_checked_radiusLT",                  //圆角-左上
        "god_checked_radiusLB",                  //圆角-左下
        "god_checked_radiusRB",                  //圆角-右下
        "god_checked_radiusRT",                  //圆角-右上
        "god_checked_solidColor",                //填充色
        "god_checked_strokeColor",               //边框色
        "god_checked_strokeWidth",               //边框宽度
        "god_checked_strokeDash",                //边框虚线宽度间隔宽度
        "god_checked_strokeDashWidth",           //边框虚线长度
        "god_checked_gradient_angle",            // 渐变方向 参考注解[GOrientation],0 上到下,4下到上 ,2 右到左,6 左到右
        "god_checked_gradient_type",             // 渐变类型, [0,1,2] 0 线性渐变,1 径向渐变,2 圆形扫描渐变
        "god_checked_gradient_startColor",       //渐变色-起点
        "god_checked_gradient_centerColor",      //填充色-中间
        "god_checked_gradient_endColor",         //渐变色-终点
        "god_checked_gradient_centerX",          //渐变色-中心点x,当渐变类型为RADIAL时有效
        "god_checked_gradient_centerY",          //渐变色-中心点Y,当渐变类型为RADIAL时有效
        "god_checked_gradient_radius",           //渐变色-半径,当渐变类型为RADIAL时有效

        "god_checkable_radius",                    //全体圆角
        "god_checkable_radiusLT",                  //圆角-左上
        "god_checkable_radiusLB",                  //圆角-左下
        "god_checkable_radiusRB",                  //圆角-右下
        "god_checkable_radiusRT",                  //圆角-右上
        "god_checkable_solidColor",                //填充色
        "god_checkable_strokeColor",               //边框色
        "god_checkable_strokeWidth",               //边框宽度
        "god_checkable_strokeDash",                //边框虚线宽度间隔宽度
        "god_checkable_strokeDashWidth",           //边框虚线长度
        "god_checkable_gradient_angle",            // 渐变方向 参考注解[GOrientation],0 上到下,4下到上 ,2 右到左,6 左到右
        "god_checkable_gradient_type",             // 渐变类型, [0,1,2] 0 线性渐变,1 径向渐变,2 圆形扫描渐变
        "god_checkable_gradient_startColor",       //渐变色-起点
        "god_checkable_gradient_centerColor",      //填充色-中间
        "god_checkable_gradient_endColor",         //渐变色-终点
        "god_checkable_gradient_centerX",          //渐变色-中心点x,当渐变类型为RADIAL时有效
        "god_checkable_gradient_centerY",          //渐变色-中心点Y,当渐变类型为RADIAL时有效
        "god_checkable_gradient_radius",           //渐变色-半径,当渐变类型为RADIAL时有效

        "god_focused_radius",                    //全体圆角
        "god_focused_radiusLT",                  //圆角-左上
        "god_focused_radiusLB",                  //圆角-左下
        "god_focused_radiusRB",                  //圆角-右下
        "god_focused_radiusRT",                  //圆角-右上
        "god_focused_solidColor",                //填充色
        "god_focused_strokeColor",               //边框色
        "god_focused_strokeWidth",               //边框宽度
        "god_focused_strokeDash",                //边框虚线宽度间隔宽度
        "god_focused_strokeDashWidth",           //边框虚线长度
        "god_focused_gradient_angle",            // 渐变方向 参考注解[GOrientation],0 上到下,4下到上 ,2 右到左,6 左到右
        "god_focused_gradient_type",             // 渐变类型, [0,1,2] 0 线性渐变,1 径向渐变,2 圆形扫描渐变
        "god_focused_gradient_startColor",       //渐变色-起点
        "god_focused_gradient_centerColor",      //填充色-中间
        "god_focused_gradient_endColor",         //渐变色-终点
        "god_focused_gradient_centerX",          //渐变色-中心点x,当渐变类型为RADIAL时有效
        "god_focused_gradient_centerY",          //渐变色-中心点Y,当渐变类型为RADIAL时有效
        "god_focused_gradient_radius",           //渐变色-半径,当渐变类型为RADIAL时有效

        "god_pressed_radius",                    //全体圆角
        "god_pressed_radiusLT",                  //圆角-左上
        "god_pressed_radiusLB",                  //圆角-左下
        "god_pressed_radiusRB",                  //圆角-右下
        "god_pressed_radiusRT",                  //圆角-右上
        "god_pressed_solidColor",                //填充色
        "god_pressed_strokeColor",               //边框色
        "god_pressed_strokeWidth",               //边框宽度
        "god_pressed_strokeDash",                //边框虚线宽度间隔宽度
        "god_pressed_strokeDashWidth",           //边框虚线长度
        "god_pressed_gradient_angle",            // 渐变方向 参考注解[GOrientation],0 上到下,4下到上 ,2 右到左,6 左到右
        "god_pressed_gradient_type",             // 渐变类型, [0,1,2] 0 线性渐变,1 径向渐变,2 圆形扫描渐变
        "god_pressed_gradient_startColor",       //渐变色-起点
        "god_pressed_gradient_centerColor",      //填充色-中间
        "god_pressed_gradient_endColor",         //渐变色-终点
        "god_pressed_gradient_centerX",          //渐变色-中心点x,当渐变类型为RADIAL时有效
        "god_pressed_gradient_centerY",          //渐变色-中心点Y,当渐变类型为RADIAL时有效
        "god_pressed_gradient_radius",           //渐变色-半径,当渐变类型为RADIAL时有效


        "god_selected_radius",                    //全体圆角
        "god_selected_radiusLT",                  //圆角-左上
        "god_selected_radiusLB",                  //圆角-左下
        "god_selected_radiusRB",                  //圆角-右下
        "god_selected_radiusRT",                  //圆角-右上
        "god_selected_solidColor",                //填充色
        "god_selected_strokeColor",               //边框色
        "god_selected_strokeWidth",               //边框宽度
        "god_selected_strokeDash",                //边框虚线宽度间隔宽度
        "god_selected_strokeDashWidth",           //边框虚线长度
        "god_selected_gradient_angle",            // 渐变方向 参考注解[GOrientation],0 上到下,4下到上 ,2 右到左,6 左到右
        "god_selected_gradient_type",             // 渐变类型, [0,1,2] 0 线性渐变,1 径向渐变,2 圆形扫描渐变
        "god_selected_gradient_startColor",       //渐变色-起点
        "god_selected_gradient_centerColor",      //填充色-中间
        "god_selected_gradient_endColor",         //渐变色-终点
        "god_selected_gradient_centerX",          //渐变色-中心点x,当渐变类型为RADIAL时有效
        "god_selected_gradient_centerY",          //渐变色-中心点Y,当渐变类型为RADIAL时有效
        "god_selected_gradient_radius",           //渐变色-半径,当渐变类型为RADIAL时有效

        "god_enabled_radius",                    //全体圆角
        "god_enabled_radiusLT",                  //圆角-左上
        "god_enabled_radiusLB",                  //圆角-左下
        "god_enabled_radiusRB",                  //圆角-右下
        "god_enabled_radiusRT",                  //圆角-右上
        "god_enabled_solidColor",                //填充色
        "god_enabled_strokeColor",               //边框色
        "god_enabled_strokeWidth",               //边框宽度
        "god_enabled_strokeDash",                //边框虚线宽度间隔宽度
        "god_enabled_strokeDashWidth",           //边框虚线长度
        "god_enabled_gradient_angle",            // 渐变方向 参考注解[GOrientation],0 上到下,4下到上 ,2 右到左,6 左到右
        "god_enabled_gradient_type",             // 渐变类型, [0,1,2] 0 线性渐变,1 径向渐变,2 圆形扫描渐变
        "god_enabled_gradient_startColor",       //渐变色-起点
        "god_enabled_gradient_centerColor",      //填充色-中间
        "god_enabled_gradient_endColor",         //渐变色-终点
        "god_enabled_gradient_centerX",          //渐变色-中心点x,当渐变类型为RADIAL时有效
        "god_enabled_gradient_centerY",          //渐变色-中心点Y,当渐变类型为RADIAL时有效
        "god_enabled_gradient_radius",           //渐变色-半径,当渐变类型为RADIAL时有效

        "god_normal_drawable",                   //normal 状态drawable
        "god_checked_drawable",                    //checked 状态drawable
        "god_checkable_drawable",                //normal 状态drawable
        "god_focused_drawable",                  //checkable 状态drawable
        "god_pressed_drawable",                  //pressed 状态drawable
        "god_selected_drawable",                 //selected 状态drawable
        "god_enabled_drawable",                  //enabled 状态drawable

    ],
    requireAll = false
)
fun View.godDrawableBackground(
    normal_radius: Float,
    normal_radiusLT: Float,
    normal_radiusLB: Float,
    normal_radiusRB: Float,
    normal_radiusRT: Float,
    @ColorInt
    normal_solidColor: Int,
    @ColorInt
    normal_strokeColor: Int,
    normal_strokeWidth: Float,
    normal_strokeDash: Float,
    normal_strokeDashWidth: Float,
    @com.org.openlib.ui.drawable.GOrientation
    normal_gradient_orientation: Int,
    @com.org.openlib.ui.drawable.GradientType
    normal_gradient_type: Int,
    @ColorInt
    normal_gradient_startColor: Int,
    @ColorInt
    normal_gradient_centerColor: Int,
    @ColorInt
    normal_gradient_endColor: Int,
    normal_gradient_centerX: Float,
    normal_gradient_centerY: Float,
    normal_gradient_radius: Float,

    checked_radius: Float,
    checked_radiusLT: Float,
    checked_radiusLB: Float,
    checked_radiusRB: Float,
    checked_radiusRT: Float,
    @ColorInt
    checked_solidColor: Int,
    @ColorInt
    checked_strokeColor: Int,
    checked_strokeWidth: Float,
    checked_strokeDash: Float,
    checked_strokeDashWidth: Float,
    @com.org.openlib.ui.drawable.GOrientation
    checked_gradient_orientation: Int,
    @com.org.openlib.ui.drawable.GradientType
    checked_gradient_type: Int,
    @ColorInt
    checked_gradient_startColor: Int,
    @ColorInt
    checked_gradient_centerColor: Int,
    @ColorInt
    checked_gradient_endColor: Int,
    checked_gradient_centerX: Float,
    checked_gradient_centerY: Float,
    checked_gradient_radius: Float,

    checkable_radius: Float,
    checkable_radiusLT: Float,
    checkable_radiusLB: Float,
    checkable_radiusRB: Float,
    checkable_radiusRT: Float,
    @ColorInt
    checkable_solidColor: Int,
    @ColorInt
    checkable_strokeColor: Int,
    checkable_strokeWidth: Float,
    checkable_strokeDash: Float,
    checkable_strokeDashWidth: Float,
    @com.org.openlib.ui.drawable.GOrientation
    checkable_gradient_orientation: Int,
    @com.org.openlib.ui.drawable.GradientType
    checkable_gradient_type: Int,
    @ColorInt
    checkable_gradient_startColor: Int,
    @ColorInt
    checkable_gradient_centerColor: Int,
    @ColorInt
    checkable_gradient_endColor: Int,
    checkable_gradient_centerX: Float,
    checkable_gradient_centerY: Float,
    checkable_gradient_radius: Float,

    focused_radius: Float,
    focused_radiusLT: Float,
    focused_radiusLB: Float,
    focused_radiusRB: Float,
    focused_radiusRT: Float,
    @ColorInt
    focused_solidColor: Int,
    @ColorInt
    focused_strokeColor: Int,
    focused_strokeWidth: Float,
    focused_strokeDash: Float,
    focused_strokeDashWidth: Float,
    @com.org.openlib.ui.drawable.GOrientation
    focused_gradient_orientation: Int,
    @com.org.openlib.ui.drawable.GradientType
    focused_gradient_type: Int,
    @ColorInt
    focused_gradient_startColor: Int,
    @ColorInt
    focused_gradient_centerColor: Int,
    @ColorInt
    focused_gradient_endColor: Int,
    focused_gradient_centerX: Float,
    focused_gradient_centerY: Float,
    focused_gradient_radius: Float,


    pressed_radius: Float,
    pressed_radiusLT: Float,
    pressed_radiusLB: Float,
    pressed_radiusRB: Float,
    pressed_radiusRT: Float,
    @ColorInt
    pressed_solidColor: Int,
    @ColorInt
    pressed_strokeColor: Int,
    pressed_strokeWidth: Float,
    pressed_strokeDash: Float,
    pressed_strokeDashWidth: Float,
    @com.org.openlib.ui.drawable.GOrientation
    pressed_gradient_orientation: Int,
    @com.org.openlib.ui.drawable.GradientType
    pressed_gradient_type: Int,
    @ColorInt
    pressed_gradient_startColor: Int,
    @ColorInt
    pressed_gradient_centerColor: Int,
    @ColorInt
    pressed_gradient_endColor: Int,
    pressed_gradient_centerX: Float,
    pressed_gradient_centerY: Float,
    pressed_gradient_radius: Float,

    selected_radius: Float,
    selected_radiusLT: Float,
    selected_radiusLB: Float,
    selected_radiusRB: Float,
    selected_radiusRT: Float,
    @ColorInt
    selected_solidColor: Int,
    @ColorInt
    selected_strokeColor: Int,
    selected_strokeWidth: Float,
    selected_strokeDash: Float,
    selected_strokeDashWidth: Float,
    @com.org.openlib.ui.drawable.GOrientation
    selected_gradient_orientation: Int,
    @com.org.openlib.ui.drawable.GradientType
    selected_gradient_type: Int,
    @ColorInt
    selected_gradient_startColor: Int,
    @ColorInt
    selected_gradient_centerColor: Int,
    @ColorInt
    selected_gradient_endColor: Int,
    selected_gradient_centerX: Float,
    selected_gradient_centerY: Float,
    selected_gradient_radius: Float,

    enabled_radius: Float,
    enabled_radiusLT: Float,
    enabled_radiusLB: Float,
    enabled_radiusRB: Float,
    enabled_radiusRT: Float,
    @ColorInt
    enabled_solidColor: Int,
    @ColorInt
    enabled_strokeColor: Int,
    enabled_strokeWidth: Float,
    enabled_strokeDash: Float,
    enabled_strokeDashWidth: Float,
    @com.org.openlib.ui.drawable.GOrientation
    enabled_gradient_orientation: Int,
    @com.org.openlib.ui.drawable.GradientType
    enabled_gradient_type: Int,
    @ColorInt
    enabled_gradient_startColor: Int,
    @ColorInt
    enabled_gradient_centerColor: Int,
    @ColorInt
    enabled_gradient_endColor: Int,
    enabled_gradient_centerX: Float,
    enabled_gradient_centerY: Float,
    enabled_gradient_radius: Float,

    normal_drawable: Drawable?,
    checked_drawable: Drawable?,
    checkable_drawable: Drawable?,
    focused_drawable: Drawable?,
    pressed_drawable: Drawable?,
    selected_drawable: Drawable?,
    enabled_drawable: Drawable?,

    ) {
    val temp = TempIndex(0)
    val normalDrawable = normal_drawable ?: createDrawable(
        temp,
        normal_radius,
        normal_radiusLT,
        normal_radiusLB,
        normal_radiusRB,
        normal_radiusRT,
        normal_solidColor,
        normal_strokeColor,
        normal_strokeWidth,
        normal_strokeDash,
        normal_strokeDashWidth,
        normal_gradient_orientation,
        normal_gradient_type,
        normal_gradient_startColor,
        normal_gradient_centerColor,
        normal_gradient_endColor,
        normal_gradient_centerX,
        normal_gradient_centerY,
        normal_gradient_radius
    )
    val checkDrawable = checked_drawable ?: createDrawable(
        temp,
        checked_radius,
        checked_radiusLT,
        checked_radiusLB,
        checked_radiusRB,
        checked_radiusRT,
        checked_solidColor,
        checked_strokeColor,
        checked_strokeWidth,
        checked_strokeDash,
        checked_strokeDashWidth,
        checked_gradient_orientation,
        checked_gradient_type,
        checked_gradient_startColor,
        checked_gradient_centerColor,
        checked_gradient_endColor,
        checked_gradient_centerX,
        checked_gradient_centerY,
        checked_gradient_radius
    )


    val checkableDrawable = checkable_drawable ?: createDrawable(
        temp,
        checkable_radius,
        checkable_radiusLT,
        checkable_radiusLB,
        checkable_radiusRB,
        checkable_radiusRT,
        checkable_solidColor,
        checkable_strokeColor,
        checkable_strokeWidth,
        checkable_strokeDash,
        checkable_strokeDashWidth,
        checkable_gradient_orientation,
        checkable_gradient_type,
        checkable_gradient_startColor,
        checkable_gradient_centerColor,
        checkable_gradient_endColor,
        checkable_gradient_centerX,
        checkable_gradient_centerY,
        checkable_gradient_radius
    )

    val focusedDrawable = focused_drawable ?: createDrawable(
        temp,
        focused_radius,
        focused_radiusLT,
        focused_radiusLB,
        focused_radiusRB,
        focused_radiusRT,
        focused_solidColor,
        focused_strokeColor,
        focused_strokeWidth,
        focused_strokeDash,
        focused_strokeDashWidth,
        focused_gradient_orientation,
        focused_gradient_type,
        focused_gradient_startColor,
        focused_gradient_centerColor,
        focused_gradient_endColor,
        focused_gradient_centerX,
        focused_gradient_centerY,
        focused_gradient_radius
    )


    val pressedDrawable = pressed_drawable ?: createDrawable(
        temp,
        pressed_radius,
        pressed_radiusLT,
        pressed_radiusLB,
        pressed_radiusRB,
        pressed_radiusRT,
        pressed_solidColor,
        pressed_strokeColor,
        pressed_strokeWidth,
        pressed_strokeDash,
        pressed_strokeDashWidth,
        pressed_gradient_orientation,
        pressed_gradient_type,
        pressed_gradient_startColor,
        pressed_gradient_centerColor,
        pressed_gradient_endColor,
        pressed_gradient_centerX,
        pressed_gradient_centerY,
        pressed_gradient_radius
    )

    val selectedDrawable = selected_drawable ?: createDrawable(
        temp,
        selected_radius,
        selected_radiusLT,
        selected_radiusLB,
        selected_radiusRB,
        selected_radiusRT,
        selected_solidColor,
        selected_strokeColor,
        selected_strokeWidth,
        selected_strokeDash,
        selected_strokeDashWidth,
        selected_gradient_orientation,
        selected_gradient_type,
        selected_gradient_startColor,
        selected_gradient_centerColor,
        selected_gradient_endColor,
        selected_gradient_centerX,
        selected_gradient_centerY,
        selected_gradient_radius
    )

    val enabledDrawable = enabled_drawable ?: createDrawable(
        temp,
        enabled_radius,
        enabled_radiusLT,
        enabled_radiusLB,
        enabled_radiusRB,
        enabled_radiusRT,
        enabled_solidColor,
        enabled_strokeColor,
        enabled_strokeWidth,
        enabled_strokeDash,
        enabled_strokeDashWidth,
        enabled_gradient_orientation,
        enabled_gradient_type,
        enabled_gradient_startColor,
        enabled_gradient_centerColor,
        enabled_gradient_endColor,
        enabled_gradient_centerX,
        enabled_gradient_centerY,
        enabled_gradient_radius
    )

    if (temp.isIndexNull()) {
        //未设置任何附加属性,或者设置的附加属性为默认值,将不会装载drawable
        return
    }

    if (temp.index == 1 && normalDrawable != null) {
        background = normalDrawable
    } else {
        //设置了多条drawable属性
        val stateListDrawable = StateListDrawable()
        //注意,addState对添加的顺序有限制,不要把大范围的放前面,否者将会被替换导致没效果

        if (checkDrawable != null) {
            stateListDrawable.addState(intArrayOf(android.R.attr.state_checked), checkDrawable)
        }
        if (checkableDrawable != null) {
            stateListDrawable.addState(
                intArrayOf(android.R.attr.state_checkable),
                checkableDrawable
            )
        }
        if (focusedDrawable != null) {
            stateListDrawable.addState(intArrayOf(android.R.attr.state_focused), focusedDrawable)
        }
        if (pressedDrawable != null) {
            stateListDrawable.addState(intArrayOf(android.R.attr.state_pressed), pressedDrawable)
        }
        if (selectedDrawable != null) {
            stateListDrawable.addState(intArrayOf(android.R.attr.state_selected), selectedDrawable)
        }
        if (enabledDrawable != null) {
            stateListDrawable.addState(intArrayOf(android.R.attr.state_enabled), enabledDrawable)
        }
        if (normalDrawable != null) {
            stateListDrawable.addState(intArrayOf(0), normalDrawable)
        } else {
            if (background != null) {
                stateListDrawable.addState(intArrayOf(0), background)
            }
        }
        background = stateListDrawable
    }
}

fun View.createDrawable(
    tempIndex: TempIndex,
    radius: Float,
    radiusLT: Float,
    radiusLB: Float,
    radiusRB: Float,
    radiusRT: Float,
    @ColorInt
    solidColor: Int,
    strokeColor: Int,
    strokeWidth: Float,
    strokeDash: Float,
    strokeDashWidth: Float,
    @com.org.openlib.ui.drawable.GOrientation
    gradientAngle: Int,
    @com.org.openlib.ui.drawable.GradientType
    gradientType: Int,
    @ColorInt
    gradientStartColor: Int,
    @ColorInt
    gradientCenterColor: Int,
    @ColorInt
    gradientEndColor: Int,
    gradient_centerX: Float,
    gradient_centerY: Float,
    gradient_radius: Float,
): Drawable? {
    if (radius.isDeft() &&
        radiusLT.isDeft()&&
        radiusLB.isDeft() &&
        radiusRB.isDeft() &&
        radiusRT.isDeft() &&
        solidColor.isDeft() &&
        strokeColor.isDeft() &&
        strokeWidth.isDeft() &&
        strokeDash.isDeft() &&
        strokeDashWidth.isDeft() &&
        gradientAngle.isDeft() &&
        gradientType.isDeft() &&
        gradientStartColor.isDeft() &&
        gradientCenterColor.isDeft() &&
        gradientEndColor.isDeft() &&
        gradient_centerX.isDeft() &&
        gradient_centerY.isDeft() &&
        gradient_radius.isDeft()
    ) {
        return null
    }

    val drawable = GradientDrawable()
    //处理圆角
    drawable.cornerRadii = if (radius == 0f) {
        floatArrayOf(
            dipF(radiusLT),
            dipF(radiusLT),
            dipF(radiusRT),
            dipF(radiusRT),
            dipF(radiusRB),
            dipF(radiusRB),
            dipF(radiusLB),
            dipF(radiusLB)
        )
    } else {
        floatArrayOf(
            dipF(radius),
            dipF(radius),
            dipF(radius),
            dipF(radius),
            dipF(radius),
            dipF(radius),
            dipF(radius),
            dipF(radius),
        )
    }
    //处理背景色或渐变色,当存在solidColor时,渐变色默认失效
    if (solidColor != 0) {
        drawable.setColor(solidColor)
    } else {
        //设置渐变色
        if (gradientStartColor != 0 || gradientCenterColor != 0 || gradientEndColor != 0) {
            drawable.colors = intArrayOf(gradientStartColor, gradientCenterColor, gradientEndColor)
            drawable.gradientType = gradientType
            drawable.orientation = mapOrientation(orientation = gradientAngle)
            if (gradientType == GradientType.RADIAL) {
                drawable.setGradientCenter(
                    if (gradient_centerX == 0f) .5f else gradient_centerX,
                    if (gradient_centerY == 0f) .5f else gradient_centerY
                )
                drawable.gradientRadius = dipF(gradient_radius)
            }
        }
    }
    tempIndex.add()
    //处理边框以及虚线
    drawable.setStroke(dipI(strokeWidth), strokeColor, dipF(strokeDash), dipF(strokeDashWidth))
    return drawable
}

/**
 * 通过int 映射渐变方向
 * @receiver GradientDrawable.Orientation
 * @param orientation Int
 * @return GradientDrawable.Orientation
 */
fun mapOrientation(@com.org.openlib.ui.drawable.GOrientation orientation: Int): GradientDrawable.Orientation {
    when (orientation) {
        GOrientation.TOP_BOTTOM -> return GradientDrawable.Orientation.TOP_BOTTOM
        GOrientation.TR_BL -> return GradientDrawable.Orientation.TR_BL
        GOrientation.RIGHT_LEFT -> return GradientDrawable.Orientation.RIGHT_LEFT
        GOrientation.BR_TL -> return GradientDrawable.Orientation.BR_TL
        GOrientation.BOTTOM_TOP -> return GradientDrawable.Orientation.BOTTOM_TOP
        GOrientation.BL_TR -> return GradientDrawable.Orientation.BL_TR
        GOrientation.LEFT_RIGHT -> return GradientDrawable.Orientation.LEFT_RIGHT
        GOrientation.TL_BR -> return GradientDrawable.Orientation.TL_BR
    }
    return GradientDrawable.Orientation.TOP_BOTTOM
}


fun View.dipF(float: Float): Float {
    return context.dip(float).toFloat()
}

fun View.dipI(float: Float): Int {
    return context.dip(float)
}

fun Float.isDeft(): Boolean {
    return this == 0f
}

fun Int.isDeft(): Boolean {
    return this == 0
}

class TempIndex(var index: Int = 0) {
    fun add() {
        index++
    }

    fun isIndexNull(): Boolean {
        return index == 0
    }
}

/**
 * 申明一个渐变方向注解
 */

@IntDef(
    value = [
        GOrientation.TOP_BOTTOM,
        GOrientation.TR_BL,
        GOrientation.RIGHT_LEFT,
        GOrientation.BR_TL,
        GOrientation.BOTTOM_TOP,
        GOrientation.BL_TR,
        GOrientation.LEFT_RIGHT,
        GOrientation.TL_BR,
    ]
)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class GOrientation {
    companion object {
        const val TOP_BOTTOM = 0
        const val TR_BL = 1
        const val RIGHT_LEFT = 2
        const val BR_TL = 3
        const val BOTTOM_TOP = 4
        const val BL_TR = 5
        const val LEFT_RIGHT = 6
        const val TL_BR = 7
    }
}

/**
 * 申明一个渐变方式注解
 */
@IntDef(
    value = [
        GradientType.LINEAR,
        GradientType.RADIAL,
        GradientType.SWEEP
    ]
)
@kotlin.annotation.Retention(AnnotationRetention.SOURCE)
annotation class GradientType {
    companion object {
        const val LINEAR = 0
        const val RADIAL = 1
        const val SWEEP = 2
    }
}
