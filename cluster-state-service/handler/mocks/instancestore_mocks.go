// Copyright 2016 Amazon.com, Inc. or its affiliates. All Rights Reserved.
//
// Licensed under the Apache License, Version 2.0 (the "License"). You may
// not use this file except in compliance with the License. A copy of the
// License is located at
//
//     http://aws.amazon.com/apache2.0/
//
// or in the "license" file accompanying this file. This file is distributed
// on an "AS IS" BASIS, WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either
// express or implied. See the License for the specific language governing
// permissions and limitations under the License.

// Automatically generated by MockGen. DO NOT EDIT!
// Source: github.com/blox/blox/cluster-state-service/handler/store (interfaces: ContainerInstanceStore)

package mocks

import (
	context "context"

	types "github.com/blox/blox/cluster-state-service/handler/store/types"
	types0 "github.com/blox/blox/cluster-state-service/handler/types"
	gomock "github.com/golang/mock/gomock"
)

// Mock of ContainerInstanceStore interface
type MockContainerInstanceStore struct {
	ctrl     *gomock.Controller
	recorder *_MockContainerInstanceStoreRecorder
}

// Recorder for MockContainerInstanceStore (not exported)
type _MockContainerInstanceStoreRecorder struct {
	mock *MockContainerInstanceStore
}

func NewMockContainerInstanceStore(ctrl *gomock.Controller) *MockContainerInstanceStore {
	mock := &MockContainerInstanceStore{ctrl: ctrl}
	mock.recorder = &_MockContainerInstanceStoreRecorder{mock}
	return mock
}

func (_m *MockContainerInstanceStore) EXPECT() *_MockContainerInstanceStoreRecorder {
	return _m.recorder
}

func (_m *MockContainerInstanceStore) AddContainerInstance(_param0 string) error {
	ret := _m.ctrl.Call(_m, "AddContainerInstance", _param0)
	ret0, _ := ret[0].(error)
	return ret0
}

func (_mr *_MockContainerInstanceStoreRecorder) AddContainerInstance(arg0 interface{}) *gomock.Call {
	return _mr.mock.ctrl.RecordCall(_mr.mock, "AddContainerInstance", arg0)
}

func (_m *MockContainerInstanceStore) AddUnversionedContainerInstance(_param0 string) error {
	ret := _m.ctrl.Call(_m, "AddUnversionedContainerInstance", _param0)
	ret0, _ := ret[0].(error)
	return ret0
}

func (_mr *_MockContainerInstanceStoreRecorder) AddUnversionedContainerInstance(arg0 interface{}) *gomock.Call {
	return _mr.mock.ctrl.RecordCall(_mr.mock, "AddUnversionedContainerInstance", arg0)
}

func (_m *MockContainerInstanceStore) DeleteContainerInstance(_param0 string, _param1 string) error {
	ret := _m.ctrl.Call(_m, "DeleteContainerInstance", _param0, _param1)
	ret0, _ := ret[0].(error)
	return ret0
}

func (_mr *_MockContainerInstanceStoreRecorder) DeleteContainerInstance(arg0, arg1 interface{}) *gomock.Call {
	return _mr.mock.ctrl.RecordCall(_mr.mock, "DeleteContainerInstance", arg0, arg1)
}

func (_m *MockContainerInstanceStore) FilterContainerInstances(_param0 string, _param1 string) ([]types0.ContainerInstance, error) {
	ret := _m.ctrl.Call(_m, "FilterContainerInstances", _param0, _param1)
	ret0, _ := ret[0].([]types0.ContainerInstance)
	ret1, _ := ret[1].(error)
	return ret0, ret1
}

func (_mr *_MockContainerInstanceStoreRecorder) FilterContainerInstances(arg0, arg1 interface{}) *gomock.Call {
	return _mr.mock.ctrl.RecordCall(_mr.mock, "FilterContainerInstances", arg0, arg1)
}

func (_m *MockContainerInstanceStore) GetContainerInstance(_param0 string, _param1 string) (*types0.ContainerInstance, error) {
	ret := _m.ctrl.Call(_m, "GetContainerInstance", _param0, _param1)
	ret0, _ := ret[0].(*types0.ContainerInstance)
	ret1, _ := ret[1].(error)
	return ret0, ret1
}

func (_mr *_MockContainerInstanceStoreRecorder) GetContainerInstance(arg0, arg1 interface{}) *gomock.Call {
	return _mr.mock.ctrl.RecordCall(_mr.mock, "GetContainerInstance", arg0, arg1)
}

func (_m *MockContainerInstanceStore) ListContainerInstances() ([]types0.ContainerInstance, error) {
	ret := _m.ctrl.Call(_m, "ListContainerInstances")
	ret0, _ := ret[0].([]types0.ContainerInstance)
	ret1, _ := ret[1].(error)
	return ret0, ret1
}

func (_mr *_MockContainerInstanceStoreRecorder) ListContainerInstances() *gomock.Call {
	return _mr.mock.ctrl.RecordCall(_mr.mock, "ListContainerInstances")
}

func (_m *MockContainerInstanceStore) StreamContainerInstances(_param0 context.Context) (chan types.ContainerInstanceErrorWrapper, error) {
	ret := _m.ctrl.Call(_m, "StreamContainerInstances", _param0)
	ret0, _ := ret[0].(chan types.ContainerInstanceErrorWrapper)
	ret1, _ := ret[1].(error)
	return ret0, ret1
}

func (_mr *_MockContainerInstanceStoreRecorder) StreamContainerInstances(arg0 interface{}) *gomock.Call {
	return _mr.mock.ctrl.RecordCall(_mr.mock, "StreamContainerInstances", arg0)
}