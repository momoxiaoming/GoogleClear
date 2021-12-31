/**
 * google sample.
 * <p>
 * https://github.com/googlesamples/android-architecture
 * </p>
 * <p>
 *     所有View实现BaseView, 所有Presenter实现BasePresenter.<br/>
 *     因为V和P之间一般都有一定的关联性，都不可能单独存在。<br/>
 *     所以按Google的方法，V和P写在一个Contract的接口内，这个Contract就
 *     做为V和P的协议管理类。
 * </p>
 */
package com.dn.vi.app.base.arch.gmvp;

